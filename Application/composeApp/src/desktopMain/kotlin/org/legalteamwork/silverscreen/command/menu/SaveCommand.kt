package org.legalteamwork.silverscreen.command.menu

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.Command
import org.legalteamwork.silverscreen.command.commandLog
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.openFileDialog
import org.legalteamwork.silverscreen.save.Project

class SaveCommand : Command {
    override val title: String = Strings.FILE_SAVE_ITEM
    override val description: String = Strings.FILE_SAVE_ITEM
    private val logger = KotlinLogging.logger {}

    private fun saveAs() {
        val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

        if (filenameSet.isNotEmpty()) {
            Project.save(filenameSet.first().path)
        }
    }

    override fun execute() {
        logger.commandLog(Strings.FILE_SAVE_ITEM)

        if (Project.hasPath)
            Project.save()
        else
            saveAs()
    }
}