package org.legalteamwork.silverscreen.rm.resource

class VideoResource(override val title: String, override val resourcePath: String) : Resource {
    var framesCount = 1 // test version
    override val numOfFrames: Int
        get() = framesCount

    fun getFrame(index: Int): Frame = TODO()
}