package org.legalteamwork.silverscreen.rm.window.effects

class BlackNWhiteFilter(
    override val videoEffect: VideoEffect,
    override val firstFrame: Int,
    override val framesLength: Int
) : VideoFilter