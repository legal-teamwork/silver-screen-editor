package org.legalteamwork.silverscreen.rm.window.effects

import org.bytedeco.javacv.FFmpegFrameFilter

interface VideoFilter {
    val videoEffect: VideoEffect
    val firstFrame: Int
    val framesLength: Int

    fun getFfmpegFilter(width: Int, height: Int): FFmpegFrameFilter
}
