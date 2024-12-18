package org.legalteamwork.silverscreen.rm.window.effects

import org.bytedeco.javacv.FFmpegFrameFilter
import org.bytedeco.opencv.opencv_core.Mat

interface VideoFilter {
    val videoEffect: VideoEffect
    val firstFrame: Int
    val framesLength: Int

    fun getFfmpegFilter(width: Int, height: Int): FFmpegFrameFilter
    fun apply(image: Mat): Mat
}
