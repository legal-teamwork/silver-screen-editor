package org.legalteamwork.silverscreen.rm.window.effects

import org.legalteamwork.silverscreen.re.ResourceOnTrack

class BlackNWhiteEffect : VideoEffect {
    override val title: String = "Black and white filter"
    override val previewPath: String = "effects/BlackNWhiteEffect_Preview.jpg"

    override fun createFilter(resourceOnTrack: ResourceOnTrack): VideoFilter =
        BlackNWhiteFilter(this, 0, resourceOnTrack.framesCount)
}