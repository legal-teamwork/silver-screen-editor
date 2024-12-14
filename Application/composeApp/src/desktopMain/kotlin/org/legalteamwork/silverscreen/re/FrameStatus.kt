package org.legalteamwork.silverscreen.re

import org.legalteamwork.silverscreen.rm.resource.VideoResource

data class FrameStatus(
    val frameNumber: Int,
    val resourceOnTrack: ResourceOnTrack?,
    val videoResource: VideoResource?
)
