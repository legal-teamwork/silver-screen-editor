package org.legalteamwork.silverscreen.render

import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameFilter
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.save.Resolution


class FiltersExportRenderer : ExportRenderer {
    override fun export(filename: String) {
//        var cachedFrameGrabberResource: ResourceOnTrack? = null
//        var cachedFrameGrabber: FFmpegFrameGrabber? = null

        val fps = Project.get { fps }
        val width = Project.get { Resolution.available[resolution].width }
        val height = Project.get { Resolution.available[resolution].height }
        val resources = VideoTrack.resourcesOnTrack.sortedBy { it.position }

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

        for (resourceOnTrack in VideoTrack.resourcesOnTrack) {
            val frameGrabber = FFmpegFrameGrabber(VideoTrack.videoResources[resourceOnTrack.id].resourcePath)
            frameGrabber.frameRate = fps
            frameGrabber.start()

            val padVideoFilter = FFmpegFrameFilter(
                "scale=width=$width:height=$height:force_original_aspect_ratio=decrease," + // Scale frame so it fits output file size
                        "pad=width=$width:height=$height:x=(ow-iw)/2:y=(oh-ih)/2:color=black",// Add black border around
                frameGrabber.imageWidth,
                frameGrabber.imageHeight,
            )
            val grayscaleFilter = FFmpegFrameFilter(
                "format=gray",
                width,
                height
            )
            padVideoFilter.frameRate = fps
            grayscaleFilter.frameRate = fps
            padVideoFilter.start()
            grayscaleFilter.start()

            while (true) {
                val grabFrame = frameGrabber.grabFrame()

                if (grabFrame == null) {
                    break
                } else {
                    padVideoFilter.push(grabFrame)
                    val paddedFrame = padVideoFilter.pull()
                    grayscaleFilter.push(paddedFrame)
                    val grayscaleFrame = grayscaleFilter.pull()
                    recorder.record(grayscaleFrame)
                }
            }

            grayscaleFilter.stop()
            grayscaleFilter.close()
            padVideoFilter.stop()
            padVideoFilter.close()
            frameGrabber.stop()
            frameGrabber.close()
        }

        recorder.stop()
        recorder.close()
    }
}