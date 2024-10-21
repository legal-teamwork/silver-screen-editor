package org.legalteamwork.silverscreen.rm.resource

class VideoResource(override val title: String, override val previewPath: String, val resourcePath: String) : Resource {

    val size: Int
        get() = TODO()

    fun getFrame(index: Int): Frame = TODO()

}