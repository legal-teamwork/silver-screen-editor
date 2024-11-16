package org.legalteamwork.silverscreen.shortcut

import androidx.compose.ui.input.key.KeyEvent
import mu.KotlinLogging

object ShortcutManager {

    private val shortcuts: MutableMap<Shortcut, (keyEvent: KeyEvent) -> Boolean> = mutableMapOf()
    private val logger = KotlinLogging.logger {}

    fun addShortcut(shortcut: Shortcut, onKeyEvent: (keyEvent: KeyEvent) -> Boolean): Boolean {
        if (shortcuts.containsKey(shortcut)) {
            return false
        } else {
            logger.info { "Initialising shortcut $shortcut" }
            shortcuts[shortcut] = onKeyEvent

            return true
        }
    }

    fun onKeyEvent(keyEvent: KeyEvent): Boolean {
        for ((shortcut, onShortcutEvent) in shortcuts) {
            if (shortcut.accepts(keyEvent)) {
                logger.info { "Triggered $shortcut shortcut" }

                return onShortcutEvent.invoke(keyEvent)
            }
        }

        return false
    }

}