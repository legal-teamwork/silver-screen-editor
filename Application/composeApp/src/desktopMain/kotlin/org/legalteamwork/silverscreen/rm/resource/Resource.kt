package org.legalteamwork.silverscreen.rm.resource

import androidx.compose.runtime.MutableState

interface Resource {
    val title: MutableState<String>
    val previewPath: String
    val properties: ResourceProperties
    var parent: FolderResource?

    fun clone(): Resource
    fun action()
}
