package org.legalteamwork.silverscreen.rm.resource

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class SimpleResource(
    override val title: MutableState<String>,
    override val previewPath: String
) : Resource {
    override val properties: ResourceProperties
        get() = ResourceProperties(
            listOf(
                ResourceProperty("Description", "Resource type", "DEBUG"),
                ResourceProperty("Description", "Title", title.value),
                ResourceProperty("Description", "Preview path", previewPath),
            )
        )

    override fun clone(): Resource = SimpleResource(mutableStateOf("${title.value} (clone)"), previewPath)
    override fun action() {}
}
