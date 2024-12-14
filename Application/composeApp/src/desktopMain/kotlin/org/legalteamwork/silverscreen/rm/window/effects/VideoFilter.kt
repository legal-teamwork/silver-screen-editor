package org.legalteamwork.silverscreen.rm.window.effects

interface VideoFilter {
    val videoEffect: VideoEffect
    val firstFrame: Int
    val framesLength: Int
}
