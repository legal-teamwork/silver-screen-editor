package org.legalteamwork.silverscreen.command.menu

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.Command
import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.command.commandLog
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.ResourceManager

class ImportCommand(
    private val resourceManager: ResourceManager,
    private val commandManager: CommandManager
) : Command {
    override val title: String = Strings.FILE_IMPORT_ITEM
    override val description: String = Strings.FILE_IMPORT_ITEM
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog(Strings.FILE_IMPORT_ITEM)

        resourceManager.addSourceTriggerActivity(commandManager)
    }

}