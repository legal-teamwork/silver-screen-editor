package org.legalteamwork.silverscreen

import androidx.compose.ui.input.key.Key

/**
 * Represents a key combination which should be pressed on a keyboard to trigger some action.
 */
data class CustomKeyShortcut(
    /**
     * Key that should be pressed to trigger an action
     */
    val key: String,

    /**
     * true if Ctrl modifier key should be pressed to trigger an action
     */
    val ctrl: Boolean = false,

    /**
     * true if Meta modifier key should be pressed to trigger an action
     * (it is Command on macOs)
     */
    val meta: Boolean = false,

    /**
     * true if Alt modifier key should be pressed to trigger an action
     */
    val alt: Boolean = false,

    /**
     * true if Shift modifier key should be pressed to trigger an action
     */
    val shift: Boolean = false,
) {
    override fun toString() = buildString {
        if (ctrl) append("Ctrl+")
        if (meta) append("Meta+")
        if (alt) append("Alt+")
        if (shift) append("Shift+")
        append(key)
    }
}