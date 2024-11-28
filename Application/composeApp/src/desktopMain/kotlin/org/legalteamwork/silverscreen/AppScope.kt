package org.legalteamwork.silverscreen

import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.rm.ResourceManager

data class AppScope(
    val commandManager: CommandManager,
    val resourceManager: ResourceManager
)
