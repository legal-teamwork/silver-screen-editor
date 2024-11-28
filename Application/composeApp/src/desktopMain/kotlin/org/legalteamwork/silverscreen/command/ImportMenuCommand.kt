package org.legalteamwork.silverscreen.command

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.ResourceManager

class ImportMenuCommand(private val resourceManager: ResourceManager) : Command {

    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog(Strings.FILE_IMPORT_ITEM)

        resourceManager.addSourceTriggerActivity()
    }
}