package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.runtime.*

@Composable
fun WithContextWindow(
    content: @Composable ContextWindowScope.() -> Unit
) {
    var popupOpen: Boolean by remember { mutableStateOf(false) }
    var popupComposable: @Composable ContextWindowScope.() -> Unit by remember { mutableStateOf({ }) }

    val scope = object : ContextWindowScope {
        override fun onContextWindowOpen(contextWindowCompose: @Composable ContextWindowScope.() -> Unit) {
            popupComposable = contextWindowCompose
            popupOpen = true
        }
        override fun onContextWindowClose() {
            popupOpen = false
        }
    }

    scope.content()

    if (popupOpen) {
        scope.popupComposable()
    }
}