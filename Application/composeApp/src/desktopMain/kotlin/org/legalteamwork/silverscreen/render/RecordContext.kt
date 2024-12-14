package org.legalteamwork.silverscreen.render

import org.bytedeco.javacv.FFmpegFrameFilter
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Java2DFrameConverter
import org.legalteamwork.silverscreen.re.ResourceOnTrack
import org.legalteamwork.silverscreen.re.VideoTrack
import java.awt.image.BufferedImage
import java.io.Closeable

class RecordContext(
    val fps: Double,
    val width: Int,
    val height: Int,
    var frameNumber: Int,
    val recorder: FFmpegFrameRecorder
) : Closeable {
    override fun close() {
        recorder.close()
    }

    fun recordBlank() {
        val frameConverter = Java2DFrameConverter()
        val blankFrame = frameConverter.convert(BufferedImage(width, height, BufferedImage.TYPE_INT_RGB))
        recorder.record(blankFrame)
        frameNumber++
    }

    fun recordResource(resourceOnTrack: ResourceOnTrack) {
        val videoResource = resourceOnTrack.let { VideoTrack.videoResources[it.id] }
        val frameGrabber = FFmpegFrameGrabber(videoResource.resourcePath)
        frameGrabber.frameRate = fps
        frameGrabber.start()

        val frameFilter = FFmpegFrameFilter(
            "scale=width=$width:height=$height:force_original_aspect_ratio=decrease," +
                    "pad=width=$width:height=$height:x=(ow-iw)/2:y=(oh-ih)/2:color=black," +
                    "format=gray",
            frameGrabber.imageWidth,
            frameGrabber.imageHeight,
        )
        frameFilter.frameRate = fps
        frameFilter.start()

        while (true) {
            frameNumber = resourceOnTrack.position + frameGrabber.frameNumber

            if (frameNumber < resourceOnTrack.position + resourceOnTrack.framesSkip) {
                // Skipped resource frame
                continue
            } else if (resourceOnTrack.isPosInside(frameNumber)) {
                // Recording frame
                val frame = frameGrabber.grabFrame() ?: throw RuntimeException()
                frameFilter.push(frame)
                recorder.record(frameFilter.pull())
            } else {
                // Next frame after resource
                break
            }
        }

        frameFilter.close()
        frameGrabber.close()
    }
}