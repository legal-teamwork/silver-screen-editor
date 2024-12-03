package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable

interface WindowBlock {
    val content: @Composable DimensionsScope.() -> Unit
}