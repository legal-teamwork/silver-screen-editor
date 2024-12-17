package org.legalteamwork.silverscreen.render

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.*
import org.bytedeco.opencv.global.opencv_core.*
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Size
import org.bytedeco.opencv.global.opencv_imgproc.*
import org.bytedeco.opencv.opencv_core.Scalar
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.save.Resolution
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger {}

private fun resizeImage(originalImage: Mat, targetWidth: Int, targetHeight: Int): Mat {
    val originalWidth = originalImage.cols()
    val originalHeight = originalImage.rows()
    if (targetWidth == originalWidth && targetHeight == originalHeight)
        return originalImage

    var newWidth = targetWidth
    var newHeight = targetHeight

    if (originalWidth * targetHeight > originalHeight * targetWidth) {
        newHeight = originalHeight * targetWidth / originalWidth
    } else {
        newWidth = originalWidth * targetHeight / originalHeight
    }

    val resizedImage = Mat()
    resize(originalImage, resizedImage, Size(newWidth, newHeight))

    if (newWidth == targetWidth && newHeight == targetHeight)
        return resizedImage

    //println("$originalWidth $originalHeight $targetWidth $targetHeight $newWidth $newHeight")
    val borderedImage = Mat()
    copyMakeBorder(resizedImage, borderedImage,
        (targetHeight - newHeight) / 2, (targetHeight - newHeight) / 2,
        (targetWidth - newWidth) / 2, (targetWidth - newWidth) / 2,
        BORDER_CONSTANT, Scalar(0.0, 0.0, 0.0, 0.0)
    )
    return borderedImage
}

/*
known issues:
- слияние аудио работает хорошо только с видео, у которых аудио дорожка в одном и том же формате
- добавление тишины работает плохо, появляются посторонние шумы и щелчки

скорее всего аудио нужно будет прогонять через отдельную порцию ffmpeg, так чтобы все было в одном и том же формате
подобным образом и генерировать тишину/склеивать получившееся
но пока что сойдет
 */
class SimpleExportRenderer(private val onProgressUpdate: (Int) -> Unit) : ExportRenderer {
    override fun export(filename: String) {
        val fps = Project.get { fps }
        val width = Project.get { Resolution.available[resolution].width }
        val height = Project.get { Resolution.available[resolution].height }
        val resources = VideoTrack.resourcesOnTrack.sortedBy { it.position }
        logger.info { "export start; expected total frames: ${resources.lastOrNull()?.run { position + framesCount } ?: 0}" }

        val exportTime = measureTimeMillis {
            val recorder = FFmpegFrameRecorder(filename, width, height)
            recorder.format = "mp4"
            recorder.frameRate = fps
            recorder.videoBitrate = Project.get { bitrate * 1000 }
            recorder.videoCodec = avcodec.AV_CODEC_ID_H264
            recorder.pixelFormat = avutil.AV_PIX_FMT_YUV420P
            recorder.start()

            val converter = OpenCVFrameConverter.ToMat()
            val blankFrame = converter.convert(Mat(width, height, CV_8UC3, Scalar(0.0, 0.0, 0.0, 0.0)))
            var lastFrame = 0
            val resourcesAmount = resources.size
            var resourceCounter = 1
            resources.forEach { resource ->
                val blankFrames = max(0, resource.position - lastFrame)

                repeat(blankFrames) { recorder.record(blankFrame) }
                lastFrame = resource.position

                val videoResource = VideoEditor.getVideoResources()[resource.id]
                val frameGrabber = FFmpegFrameGrabber(videoResource.resourcePath)
                frameGrabber.start()

                var cachedFrame: Frame? = null
                var lastSourceFrame = -1
                //println(videoResource.numberOfFrames)
                (resource.framesSkip..<resource.framesSkip + resource.framesCount).forEach { frameNo ->
                    val nextFrame = videoResource.fromProjectFPS(frameNo)
                    //logger.info { "relative frame $frameNo; source frame $nextFrame" }
                    val start = System.currentTimeMillis()
                    if (nextFrame > lastSourceFrame) {
                        try {
                            repeat(nextFrame - lastSourceFrame - 1) { frameGrabber.grabImage() }
                            val frame = frameGrabber.grabImage()
                            val image = converter.convert(frame)
                            val resizedImage = resizeImage(image, width, height)
                            val resizedFrame = converter.convert(resizedImage)
                            cachedFrame = resizedFrame
                            lastSourceFrame = nextFrame
                            logger.info { "updated cached frame" }
                        } catch (_: Exception) {}
                    }
                    recorder.record(cachedFrame)
                    logger.info { "processing frame took ${System.currentTimeMillis() - start} ms" }
                }
                frameGrabber.stop()
                frameGrabber.close()
                logger.info { "export: $blankFrames padded frames, ${resource.framesCount} actual frames" }
                lastFrame += resource.framesCount
                val percentage = ((resourceCounter.toDouble() / resourcesAmount) * 100).roundToInt()
                onProgressUpdate(percentage)
                resourceCounter += 1
            }
            recorder.stop()
            recorder.close()
            onProgressUpdate(100)
        }

        logger.info { "export done; took $exportTime ms" }
    }
}