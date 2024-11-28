package org.legalteamwork.silverscreen.command

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.resources.Strings

class ExportMenuCommand : Command {

    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog(Strings.FILE_EXPORT_ITEM)
    }
}