package org.legalteamwork.silverscreen.rm.window.effects

import org.legalteamwork.silverscreen.re.ResourceOnTrack

interface VideoEffect {
    val title: String
    val previewPath: String

    fun createFilter(resourceOnTrack: ResourceOnTrack): VideoFilter
}