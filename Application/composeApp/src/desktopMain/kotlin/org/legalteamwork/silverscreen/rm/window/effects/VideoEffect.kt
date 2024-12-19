package org.legalteamwork.silverscreen.rm.window.effects

import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.re.ResourceOnTrack

@Serializable
sealed interface VideoEffect {
    val title: String
    val previewPath: String

    fun createFilter(resourceOnTrack: ResourceOnTrack): VideoFilter
}