package org.legalteamwork.silverscreen.shortcut

import androidx.compose.ui.input.key.KeyEvent
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Данный класс отвечает за глобальные шорткаты окна
 */
class ShortcutManager {

    private val shortcuts: MutableMap<Shortcut, (keyEvent: KeyEvent) -> Boolean> = mutableMapOf()
    private val logger = KotlinLogging.logger {}

    /**
     * Добавляет к шорткату коллбек только если он не был ранее определён.
     *
     * @param[shortcut] шорткат
     * @param[onKeyEvent] коллбек, который будет вызываться, когда пользователь триггерит шорткат
     *
     * @return true если шорткат успешно добавлен
     */
    fun addShortcut(shortcut: Shortcut, onKeyEvent: (keyEvent: KeyEvent) -> Boolean): Boolean {
        if (shortcuts.containsKey(shortcut)) {
            return false
        } else {
            logger.info { "Initialising shortcut $shortcut" }
            shortcuts[shortcut] = onKeyEvent

            return true
        }
    }

    /**
     * Прогоняет ивент по всем шорткатам в поисках, есть ли тот, который затриггерили.
     * Если он есть - вызывает соответствующий коллбек.
     *
     * @param[keyEvent] входящий ивент
     *
     * @return результат коллбека или false
     */
    fun onKeyEvent(keyEvent: KeyEvent): Boolean {
        for ((shortcut, onShortcutEvent) in shortcuts) {
            if (shortcut.accepts(keyEvent)) {
                logger.debug { "Triggered $shortcut shortcut" }

                return onShortcutEvent.invoke(keyEvent)
            }
        }

        return false
    }

}
