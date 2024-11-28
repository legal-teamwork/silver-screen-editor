package org.legalteamwork.silverscreen.command.menu

import org.legalteamwork.silverscreen.command.Command
import org.legalteamwork.silverscreen.command.commandLog
import org.legalteamwork.silverscreen.menu.logger
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.openFileDialog
import org.legalteamwork.silverscreen.save.Project

class OpenProjectMenuCommand : Command {
    override fun execute() {
        logger.commandLog(Strings.FILE_OPEN_ITEM)

        val filenameSet = openFileDialog(null, "Open project", listOf("json"), false)

        if (filenameSet.isNotEmpty()) {
            Project.load(filenameSet.first().path)
        }
    }

}