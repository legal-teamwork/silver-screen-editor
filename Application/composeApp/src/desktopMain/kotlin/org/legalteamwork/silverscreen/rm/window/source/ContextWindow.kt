package org.legalteamwork.silverscreen.rm.window.source

data class ContextWindow(
    val id: Int,
    val data: ContextWindowData
) {
    companion object {
        const val CONTEXT_MENU = 1
        const val PROPERTIES = 2
    }
}
