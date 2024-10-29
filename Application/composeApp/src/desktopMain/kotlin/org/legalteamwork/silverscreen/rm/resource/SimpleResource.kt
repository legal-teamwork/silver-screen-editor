package org.legalteamwork.silverscreen.rm.resource

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class SimpleResource(override val title: MutableState<String>, override val previewPath: String) : Resource {
    override fun clone(): Resource = SimpleResource(mutableStateOf("${title.value} (clone)"), previewPath)
}
