package org.legalteamwork.silverscreen.command

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.openFileDialog
import org.legalteamwork.silverscreen.save.Project

class SaveAsMenuCommand : Command {

    private val logger = KotlinLogging.logger {}

    private fun saveAs() {
        val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

        if (filenameSet.isNotEmpty()) {
            Project.save(filenameSet.first().path)
        }
    }

    override fun execute() {
        logger.commandLog(Strings.FILE_SAVE_AS_ITEM)

        saveAs()
    }
}