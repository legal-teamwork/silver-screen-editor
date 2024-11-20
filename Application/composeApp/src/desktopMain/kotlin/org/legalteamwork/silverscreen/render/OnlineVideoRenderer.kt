package org.legalteamwork.silverscreen.render

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import java.io.Closeable

class OnlineVideoRenderer : Closeable {

    var videoResource: VideoResource? = null
        private set
    private var frameGrabber: FFmpegFrameGrabber? = null
    private val logger = KotlinLogging.logger {}

    fun setVideoResource(resource: VideoResource) {
        // Close previous frameGrabber
        frameGrabber?.let {
            it.stop()
            it.close()
        }

        // Update frameGrabber
        videoResource = resource
        frameGrabber = FFmpegFrameGrabber(resource.resourcePath).apply {
            format = "mp4"
        }.also(FFmpegFrameGrabber::start)
    }

    fun getFrameNumberByTimestamp(timestamp: Long): Int =
        (timestamp * (frameGrabber?.frameRate ?: throw Exception("Invalid method usage")) / 1000).toInt()

    fun getTimestampByFrameNumber(frameNumber: Int): Long =
        (frameNumber * 1000 / (frameGrabber?.frameRate ?: throw Exception("Invalid method usage"))).toLong()

    fun setVideoFrame(frameNumber: Int) {
        frameGrabber?.frameNumber = frameNumber
    }

    fun setVideoFrameByTimestamp(timestamp: Long) {
        frameGrabber?.apply {
            frameNumber = getFrameNumberByTimestamp(timestamp)
        }
    }

    fun grabImage(): Frame =
        frameGrabber?.grabImage() ?: throw Exception("Frame is NULL")

    fun grabVideoFrame(frameNumber: Int): Frame = frameGrabber?.let { frameGrabber ->
        if (frameGrabber.frameNumber <= frameNumber) {
            while (frameGrabber.frameNumber < frameNumber) {
                logger.debug { "Moving forwards over frame numbers: ${frameGrabber.frameNumber} -> $frameNumber" }

                frameGrabber.grabImage()
            }

            frameGrabber.grabImage()
        } else {
            logger.debug { "Moving backwards over frame numbers: ${frameGrabber.frameNumber} -> $frameNumber" }

            frameGrabber.frameNumber = frameNumber
            frameGrabber.grabImage()
        }
    } ?: throw Exception("Frame is NULL")

    fun grabVideoFrameByTimestamp(timestamp: Long): Frame =
        grabVideoFrame(getFrameNumberByTimestamp(timestamp))

    override fun close() {
        frameGrabber?.let {
            it.stop()
            it.close()
        }
    }

}