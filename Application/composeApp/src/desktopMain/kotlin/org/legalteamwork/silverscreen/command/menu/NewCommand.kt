package org.legalteamwork.silverscreen.command.menu

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.Command
import org.legalteamwork.silverscreen.command.commandLog
import org.legalteamwork.silverscreen.resources.Strings

class NewCommand : Command {
    override val title: String = Strings.FILE_NEW_ITEM
    override val description: String = Strings.FILE_NEW_ITEM
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog(Strings.FILE_NEW_ITEM)
    }

}