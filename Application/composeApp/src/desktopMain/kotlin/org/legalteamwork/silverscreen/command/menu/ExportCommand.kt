package org.legalteamwork.silverscreen.command.menu

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.legalteamwork.silverscreen.command.Command
import org.legalteamwork.silverscreen.command.commandLog
import org.legalteamwork.silverscreen.render.ExportRenderer
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.openFileDialog

class ExportCommand(
    private val onStartExport: () -> Unit,
    private val onProgressUpdate: (Int) -> Unit,
    private val onFinishExport: () -> Unit
) : Command {
    override val title: String = Strings.FILE_EXPORT_ITEM
    override val description: String = Strings.FILE_EXPORT_ITEM
    private val exportStrategy: (onProgressUpdate: (Int) -> Unit) -> ExportRenderer = { onProgressUpdate ->
        SimpleExportRenderer(onProgressUpdate)
    }
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog(Strings.FILE_EXPORT_ITEM)
        GlobalScope.launch {
            onStartExport()
            val filenameSet = openFileDialog(null, "Export to video", listOf("mp4"), false)

            if (filenameSet.isNotEmpty()) {
                val renderer = exportStrategy { progress ->
                    onProgressUpdate(progress)
                }
                renderer.export(filenameSet.first().toString())
            }
            withContext(Dispatchers.Main) {
                onFinishExport()
            }
        }
    }
}