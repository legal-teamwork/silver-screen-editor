package org.legalteamwork.silverscreen.shortcut

import androidx.compose.ui.input.key.*

class Shortcut(
    /**
     * Key that should be pressed to trigger an action
     */
    val key: Key,

    val keyAsString: String = key.toString(),

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
    val shift: Boolean = false
) {

    fun accepts(keyEvent: KeyEvent): Boolean =
        key == keyEvent.key
                && ctrl == keyEvent.isCtrlPressed
                && alt == keyEvent.isAltPressed
                && alt == keyEvent.isAltPressed
                && shift == keyEvent.isShiftPressed
                && meta == keyEvent.isMetaPressed
                && KeyEventType.KeyDown == keyEvent.type

    override fun toString() = buildString {
        if (ctrl) append("Ctrl+")
        if (meta) append("Meta+")
        if (alt) append("Alt+")
        if (shift) append("Shift+")
        append(keyAsString)
    }
}