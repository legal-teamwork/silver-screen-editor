package org.legalteamwork.silverscreen.rm.window.effects

import org.bytedeco.javacv.FFmpegFrameFilter

class GrayscaleFilter(
    override val videoEffect: VideoEffect,
    override val firstFrame: Int,
    override val framesLength: Int
) : VideoFilter {
    override fun getFfmpegFilter(width: Int, height: Int): FFmpegFrameFilter = FFmpegFrameFilter(
        "format=gray", width, height
    )
}