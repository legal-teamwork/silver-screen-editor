package org.legalteamwork.silverscreen.rm.resource

import androidx.compose.runtime.MutableState
import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.rm.serializers.MutableStateStringSerializer

@Serializable
sealed interface Resource {
    @Serializable(with = MutableStateStringSerializer::class)
    val title: MutableState<String>
    val previewPath: String
    val properties: ResourceProperties
    var parent: FolderResource?

    fun clone(): Resource
    fun action()
}
