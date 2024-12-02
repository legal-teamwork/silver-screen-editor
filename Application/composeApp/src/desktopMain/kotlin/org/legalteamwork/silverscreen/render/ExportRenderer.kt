package org.legalteamwork.silverscreen.render

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.*
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.save.Project
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage

private val logger = KotlinLogging.logger {}

private fun resizeImage(originalImage: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
    // Calculate the aspect ratio
    val originalWidth = originalImage.width
    val originalHeight = originalImage.height
    if (targetWidth == originalWidth && targetHeight == originalHeight)
        return originalImage

    val aspectRatio = originalWidth.toDouble() / originalHeight.toDouble()

    // Determine new dimensions while preserving aspect ratio
    var newWidth = targetWidth
    var newHeight = targetHeight

    if (originalWidth > originalHeight) {
        newHeight = (targetWidth / aspectRatio).toInt()
    } else {
        newWidth = (targetHeight * aspectRatio).toInt()
    }

    // Create a new BufferedImage with the new dimensions
    val resizedImage = BufferedImage(targetWidth, targetHeight, originalImage.type)

    // Draw the original image into the resized image
    val g2d: Graphics2D = resizedImage.createGraphics()
    g2d.drawImage(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST),
        (targetWidth - newWidth) / 2, (targetHeight - newHeight) / 2, null)
    g2d.dispose()

    return resizedImage
}

class ExportRenderer {
    fun export(filename: String) {
        val fps = Project.get { fps }
        val width = Project.get { resolution.width }
        val height = Project.get { resolution.height }
        val resources = VideoEditor.VideoTrack.resourcesOnTrack.sortedBy { it.position }
        val length = resources.lastOrNull()?.run { position + framesCount } ?: 0
        logger.info { "export start; total frames: $length" }

        val recorder = FFmpegFrameRecorder(filename, width, height)
        recorder.format = "mp4"
        recorder.frameRate = fps
        recorder.videoBitrate = Project.get { bitrate * 1000 }
        recorder.videoCodec = avcodec.AV_CODEC_ID_H264
        recorder.pixelFormat = avutil.AV_PIX_FMT_YUV420P
        VideoEditor.VideoTrack.videoResources.firstOrNull()?.let {
            val grabber = FFmpegFrameGrabber(it.resourcePath)
            grabber.start()
            recorder.audioCodec = grabber.audioCodec
            recorder.audioBitrate = grabber.audioBitrate
            recorder.audioChannels = grabber.audioChannels
            recorder.sampleRate = grabber.sampleRate
            grabber.stop()
        }
        recorder.start()

        val converter = Java2DFrameConverter()
        var lastFrame = 0
        resources.forEach { resource ->
            val blankFrames = resource.position - lastFrame
            logger.info { "export: padding $blankFrames blank frames"}
            repeat(blankFrames) {
                recorder.record(converter.convert(BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)))
            }
            lastFrame = resource.position

            val videoResource = VideoEditor.getVideoResources()[resource.id]
            val frameGrabber = FFmpegFrameGrabber(videoResource.resourcePath)
            frameGrabber.start()
            val sourceFPS = frameGrabber.frameRate

            var timestamp = 0L
            var cachedFrame: Frame? = null
            var lastSourceFrame = -1

            while (true) {
                val nextFrame = (timestamp * sourceFPS / 1000).toInt()
                if (nextFrame >= resource.framesCount)
                    break
                if (nextFrame > lastSourceFrame) {
                    repeat(nextFrame - lastSourceFrame - 1) { frameGrabber.grabImage() }
                    val frame = frameGrabber.grabImage()
                    val image = converter.convert(frame)
                    val resizedImage = resizeImage(image, width, height)
                    val resizedFrame = converter.convert(resizedImage)
                    cachedFrame = resizedFrame
                    lastSourceFrame = nextFrame
                }
                recorder.record(cachedFrame)
                timestamp += (1000 / fps).toLong()
                lastFrame++
            }
            frameGrabber.stop()
            frameGrabber.close()
        }
        logger.info { "export done" }
        recorder.stop()
        recorder.close()
    }
}