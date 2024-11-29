package org.legalteamwork.silverscreen

import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.menu.MenuBarData
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.shortcut.ShortcutManager

data class AppScope(
    val commandManager: CommandManager,
    val resourceManager: ResourceManager,
    val shortcutManager: ShortcutManager,

    val menuBarData: MenuBarData,
)
