package org.legalteamwork.silverscreen.rm.window.effects

import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.re.ResourceOnTrack

@Serializable
class GrayscaleEffect : VideoEffect {
    override val title: String = "Grayscale"
    override val previewPath: String = "effects/Grayscale_Preview.jpg"

    override fun createFilter(resourceOnTrack: ResourceOnTrack): VideoFilter =
        GrayscaleFilter(this, 0, resourceOnTrack.framesCount)
}