package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.runtime.*

class ContextMenuScope {
    val contextMenuState = mutableStateOf<(@Composable () -> Unit)?>(null)
}