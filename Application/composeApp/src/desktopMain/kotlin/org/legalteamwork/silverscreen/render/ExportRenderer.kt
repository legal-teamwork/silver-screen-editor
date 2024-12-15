package org.legalteamwork.silverscreen.render

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.*
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.save.Resolution
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.roundToInt

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

/*
known issues:
- слияние аудио работает хорошо только с видео, у которых аудио дорожка в одном и том же формате
- добавление тишины работает плохо, появляются посторонние шумы и щелчки

скорее всего аудио нужно будет прогонять через отдельную порцию ffmpeg, так чтобы все было в одном и том же формате
подобным образом и генерировать тишину/склеивать получившееся
но пока что сойдет
 */
class ExportRenderer(private val onProgressUpdate: (Int) -> Unit) {
    fun export(filename: String) {
        val fps = Project.get { fps }
        val width = Project.get { Resolution.available[resolution].width }
        val height = Project.get { Resolution.available[resolution].height }
        val resources = VideoTrack.resourcesOnTrack.sortedBy { it.position }
        logger.info { "export start; expected total frames: ${resources.lastOrNull()?.run { position + framesCount } ?: 0}" }

        val recorder = FFmpegFrameRecorder(filename, width, height)
        recorder.format = "mp4"
        recorder.frameRate = fps
        recorder.videoBitrate = Project.get { bitrate * 1000 }
        recorder.videoCodec = avcodec.AV_CODEC_ID_H264
        recorder.pixelFormat = avutil.AV_PIX_FMT_YUV420P
        VideoTrack.videoResources.firstOrNull()?.let {
            val grabber = FFmpegFrameGrabber(it.resourcePath)
            grabber.start()
            recorder.audioCodec = grabber.audioCodec
            recorder.audioBitrate = grabber.audioBitrate
            recorder.audioChannels = grabber.audioChannels
            recorder.sampleRate = grabber.sampleRate
            grabber.stop()
        }
        recorder.start()

        fun next(grabber: FFmpegFrameGrabber): Frame {
            while (true) {
                val frame = grabber.grab()
                //println(frame.type)
                if (frame.type == Frame.Type.AUDIO)
                    recorder.record(frame)
                else
                    return frame
            }
        }

        val converter = Java2DFrameConverter()
        var lastFrame = 0
        val resourcesAmount = resources.size
        var resourceCounter = 1
        resources.forEach { resource ->
            val blankFrames = max(0, resource.position - lastFrame)

            repeat(blankFrames) {
                recorder.record(converter.convert(BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)))
            }
            val length = (recorder.sampleRate * blankFrames / fps).toInt()
            println("$blankFrames $length")
            val bufferList = mutableListOf<ByteBuffer>()
            repeat(recorder.audioChannels) {
                val buffer = ByteBuffer.allocate(length)
                bufferList.add(buffer)
            }
            recorder.recordSamples(*bufferList.toTypedArray())
            lastFrame = resource.position

            val videoResource = VideoEditor.getVideoResources()[resource.id]
            val frameGrabber = FFmpegFrameGrabber(videoResource.resourcePath)
            frameGrabber.start()
            val sourceFPS = frameGrabber.frameRate

            var frames = 0
            var timestamp = 0L
            var cachedFrame: Frame? = null
            var lastSourceFrame = -1
            while (true) {
                val nextFrame = (timestamp * sourceFPS / 1000).toInt()
                if (nextFrame >= resource.framesCount)
                    break
                if (nextFrame > lastSourceFrame) {
                    repeat(nextFrame - lastSourceFrame - 1) { next(frameGrabber) }
                    val frame = next(frameGrabber)
                    val image = converter.convert(frame)
                    val resizedImage = resizeImage(image, width, height)
                    val resizedFrame = converter.convert(resizedImage)
                    cachedFrame = resizedFrame
                    lastSourceFrame = nextFrame
                }
                recorder.record(cachedFrame)
                timestamp += (1000 / fps).toLong()
                frames++
            }
            frameGrabber.stop()
            frameGrabber.close()
            logger.info { "export: $blankFrames padded frames, $frames actual frames" }
            lastFrame += frames
            val percentage = ((resourceCounter.toDouble() / resourcesAmount) * 100).roundToInt()
            onProgressUpdate(percentage)
            resourceCounter += 1
        }
        logger.info { "export done" }
        recorder.stop()
        recorder.close()
        onProgressUpdate(100)
    }
}
