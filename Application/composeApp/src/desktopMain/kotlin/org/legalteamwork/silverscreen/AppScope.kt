package org.legalteamwork.silverscreen

import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.window.effects.EffectsManager
import org.legalteamwork.silverscreen.shortcut.ShortcutManager

data class AppScope(
    val commandManager: CommandManager,
    val resourceManager: ResourceManager,
    val shortcutManager: ShortcutManager,
    val effectsManager: EffectsManager
)
