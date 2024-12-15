package org.legalteamwork.silverscreen.shortcut

import androidx.compose.ui.input.key.*

data class Shortcut(
    /**
     * Key that should be pressed to trigger an action
     */
    val key: Key,

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
        append(KEY_AS_STRING[key] ?: key.toString())
    }

    companion object {
        private val KEY_AS_STRING = mapOf(
            Key.A to 'A',
            Key.B to 'B',
            Key.C to 'C',
            Key.D to 'D',
            Key.E to 'E',
            Key.F to 'F',
            Key.G to 'G',
            Key.H to 'H',
            Key.I to 'I',
            Key.J to 'J',
            Key.K to 'K',
            Key.L to 'L',
            Key.M to 'M',
            Key.N to 'N',
            Key.O to 'O',
            Key.P to 'P',
            Key.Q to 'Q',
            Key.R to 'R',
            Key.S to 'S',
            Key.T to 'T',
            Key.U to 'U',
            Key.V to 'V',
            Key.W to 'W',
            Key.X to 'X',
            Key.Y to 'Y',
            Key.Z to 'Z',
            Key.Delete to "Delete"
        )
    }
}
