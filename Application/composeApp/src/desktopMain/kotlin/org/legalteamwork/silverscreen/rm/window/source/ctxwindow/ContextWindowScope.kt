package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.runtime.Composable

interface ContextWindowScope {
    fun onContextWindowOpen(contextWindowCompose: @Composable ContextWindowScope.() -> Unit)
    fun onContextWindowClose()
}