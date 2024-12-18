package org.legalteamwork.silverscreen.rm.window.effects

import kotlinx.serialization.Serializable
import org.bytedeco.javacv.FFmpegFrameFilter
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.global.opencv_imgproc.*

@Serializable
class GrayscaleFilter(
    override val videoEffect: VideoEffect,
    override val firstFrame: Int,
    override val framesLength: Int
) : VideoFilter {
    override fun getFfmpegFilter(width: Int, height: Int): FFmpegFrameFilter = FFmpegFrameFilter(
        "format=gray", width, height
    )

    override fun apply(image: Mat): Mat {
        val dest = Mat()
        cvtColor(image, dest, COLOR_BGR2GRAY)
        return dest
    }
}