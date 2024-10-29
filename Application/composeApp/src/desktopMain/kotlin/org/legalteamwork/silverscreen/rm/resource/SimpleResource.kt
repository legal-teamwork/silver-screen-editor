package org.legalteamwork.silverscreen.rm.resource

import androidx.compose.runtime.MutableState

data class SimpleResource(override val title: MutableState<String>, override val previewPath: String) : Resource
