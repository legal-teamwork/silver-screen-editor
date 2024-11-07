package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

data class ContextWindow(
    val id: ContextWindowId,
    val data: ContextWindowData
) {
    enum class ContextWindowId {
        CONTEXT_MENU,
        PROPERTIES,
        MOVE_TO,
        COPY_TO,
        NEW_FOLDER,
    }
}
