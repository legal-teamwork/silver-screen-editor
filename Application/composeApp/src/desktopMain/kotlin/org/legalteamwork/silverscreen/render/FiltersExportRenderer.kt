package org.legalteamwork.silverscreen.render

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.*
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.save.Resolution

class FiltersExportRenderer : ExportRenderer {
    private val logger = KotlinLogging.logger { }

    override fun export(filename: String) {
        FFmpegLogCallback.set()

        val fps = Project.get { fps }
        val width = Project.get { Resolution.available[resolution].width }
        val height = Project.get { Resolution.available[resolution].height }

        val recorder = FFmpegFrameRecorder(filename, width, height)
        recorder.format = "mp4"
        recorder.frameRate = fps
        recorder.videoCodec = avcodec.AV_CODEC_ID_H264
        recorder.audioCodec = avcodec.AV_CODEC_ID_MP3
        recorder.videoBitrate = Project.get { bitrate * 1000 }
        recorder.audioBitrate = 128_000 // 128 kb/s
        recorder.pixelFormat = avutil.AV_PIX_FMT_YUV420P
        recorder.audioChannels = 1 // channels
        recorder.sampleRate = 48000 // Hz
        recorder.start()

        val context = RecordContext(fps, width, height, 0, recorder)

        while (context.frameNumber <= VideoTrack.lengthInFrames) {
            val frameStatus = VideoTrack.getFrameStatus(context.frameNumber)
            val resourceOnTrack = frameStatus.resourceOnTrack

            if (resourceOnTrack == null) {
                context.recordBlank()
            } else {
                context.recordResource(resourceOnTrack)
            }
        }

        logger.info { "Completed export render" }
        recorder.close()
    }
}