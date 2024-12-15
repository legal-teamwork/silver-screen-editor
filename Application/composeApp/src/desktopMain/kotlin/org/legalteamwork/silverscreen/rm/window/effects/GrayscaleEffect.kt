package org.legalteamwork.silverscreen.rm.window.effects

import org.legalteamwork.silverscreen.re.ResourceOnTrack

class GrayscaleEffect : VideoEffect {
    override val title: String = "Grayscale"
    override val previewPath: String = "effects/Grayscale_Preview.jpg"

    override fun createFilter(resourceOnTrack: ResourceOnTrack): VideoFilter =
        GrayscaleFilter(this, 0, resourceOnTrack.framesCount)
}