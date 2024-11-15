package org.legalteamwork.silverscreen

import androidx.compose.ui.input.key.KeyEvent
import mu.KotlinLogging

object ShortcutManager {

    private val shortcuts: MutableList<Pair<Shortcut, (keyEvent: KeyEvent) -> Boolean>> = mutableListOf()
    private val logger = KotlinLogging.logger {}

    fun addShortcut(shortcut: Shortcut, onKeyEvent: (keyEvent: KeyEvent) -> Boolean) {
        shortcuts.add(shortcut to onKeyEvent)
    }

    fun onKeyEvent(keyEvent: KeyEvent): Boolean {
        for (shortcut in shortcuts) {
            if (shortcut.first.accepts(keyEvent)) {
                logger.info { "Triggered ${shortcut.first} shortcut" }

                return shortcut.second(keyEvent)
            }
        }

        return false
    }

}