package org.legalteamwork.silverscreen.command.menu

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.Command
import org.legalteamwork.silverscreen.command.commandLog
import org.legalteamwork.silverscreen.render.ExportRenderer
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.openFileDialog

class ExportCommand : Command {
    override val title: String = Strings.FILE_EXPORT_ITEM
    override val description: String = Strings.FILE_EXPORT_ITEM
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog(Strings.FILE_EXPORT_ITEM)

        val filenameSet = openFileDialog(null, "Export to video", listOf("mp4"), false)

        if (filenameSet.isNotEmpty()) {
            ExportRenderer().export(filenameSet.first().toString())
        }
    }
}