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
    val fps: Double, val width: Int, val height: Int, var frameNumber: Int, val recorder: FFmpegFrameRecorder
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
        val frameGrabber = FFmpegFrameGrabber(VideoTrack.videoResources[resourceOnTrack.id].resourcePath)
        val scaleFilter = FFmpegFrameFilter(
            "scale=width=$width:height=$height:force_original_aspect_ratio=decrease," +
                    "pad=width=$width:height=$height:x=(ow-iw)/2:y=(oh-ih)/2:color=black",
            frameGrabber.imageWidth,
            frameGrabber.imageHeight,
        )
        val resourceContext = RecordResourceContextBuilder(fps)
            .setFrameGrabber(frameGrabber)
            .addFilter(scaleFilter)
            .addFilters(resourceOnTrack.filters.map { it.getFfmpegFilter(width, height) })
            .build()


        while (true) {
            frameNumber = resourceOnTrack.position + frameGrabber.frameNumber

            if (frameNumber < resourceOnTrack.position + resourceOnTrack.framesSkip) {
                // Skipped resource frame
                continue
            } else if (resourceOnTrack.isPosInside(frameNumber)) {
                // Recording frame
                var frame = resourceContext.frameGrabber.grabFrame() ?: throw RuntimeException()

                for (ffmpegFrameFilter in resourceContext.filters) {
                    ffmpegFrameFilter.push(frame)
                    frame = ffmpegFrameFilter.pull()
                }

                recorder.record(frame)
            } else {
                // Next frame after resource
                break
            }
        }

        resourceContext.close()
    }
}