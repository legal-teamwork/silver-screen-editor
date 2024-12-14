package org.legalteamwork.silverscreen.render

import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.*
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.save.Resolution
import java.awt.image.BufferedImage


class FiltersExportRenderer : ExportRenderer {
    private val converter = Java2DFrameConverter()

    override fun export(filename: String) {
        FFmpegLogCallback.set()

        val fps = Project.get { fps }
        val width = Project.get { Resolution.available[resolution].width }
        val height = Project.get { Resolution.available[resolution].height }

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
            grabber.close()
        }
        recorder.start()

        var frameNumber = 0

        while(frameNumber <= VideoTrack.lengthInFrames) {
            println("Trying $frameNumber/${VideoTrack.lengthInFrames}")
            val frameStatus = VideoTrack.getFrameStatus(frameNumber)
            val resourceOnTrack = frameStatus.resourceOnTrack

            if (resourceOnTrack == null) {
                println("- empty")
                val blankFrame = converter.convert(BufferedImage(width, height, BufferedImage.TYPE_INT_RGB))
                recorder.record(blankFrame)
                frameNumber++
            } else {
                println("- ${frameStatus.videoResource?.title}")
                val frameGrabber = FFmpegFrameGrabber(frameStatus.videoResource!!.resourcePath)
                frameGrabber.frameRate = fps
                frameGrabber.start()
                val frameFilter = FFmpegFrameFilter(
                    "scale=width=$width:height=$height:force_original_aspect_ratio=decrease," + // Scale frame so it fits output file size
                            "pad=width=$width:height=$height:x=(ow-iw)/2:y=(oh-ih)/2:color=black,"+
                            "format=gray",// Add black border around
                    frameGrabber.imageWidth,
                    frameGrabber.imageHeight,
                )
                frameFilter.frameRate = fps
                frameFilter.start()

                while (true) {
                    val currentFrameNumber = frameGrabber.frameNumber
                    frameNumber = resourceOnTrack.position + currentFrameNumber

                    if (frameNumber < resourceOnTrack.framesSkip) {
                        println("-- Skip $frameNumber/$currentFrameNumber")
                        continue
                    } else if (resourceOnTrack.isPosInside(frameNumber)) {
                        println("-- Got $frameNumber/$currentFrameNumber")
                        val frame = frameGrabber.grabFrame() ?: throw RuntimeException()
                        frameFilter.push(frame)
                        recorder.record(frameFilter.pull())
                    } else {
                        println("-- End $frameNumber/$currentFrameNumber")
                        break
                    }
                }

                frameFilter.close()
                frameGrabber.close()
            }
        }

        println("============ Completed ==============")
        recorder.close()
    }
}