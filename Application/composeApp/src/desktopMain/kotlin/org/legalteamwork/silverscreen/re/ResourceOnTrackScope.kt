package org.legalteamwork.silverscreen.re

import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.rm.ResourceManager

data class ResourceOnTrackScope(
    val commandManager: CommandManager,
    val resourceManager: ResourceManager,
    val resourceOnTrack: ResourceOnTrack
)