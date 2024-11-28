package org.legalteamwork.silverscreen.rm

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.AddResourceCommand
import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.rm.ResourceManager.currentFolder
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import java.awt.datatransfer.DataFlavor
import java.io.File

class MainWindowDragAndDropTarget(
    private val resourceManager: ResourceManager,
    private val commandManager: CommandManager
) : DragAndDropTarget {

    private val logger = KotlinLogging.logger {}

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onDrop(event: DragAndDropEvent): Boolean {

        logger.info { "Files started dropping in the window" }
        val files =
            (event.awtTransferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>).filter { it.extension == "mp4" }

        if (files.isEmpty()) {
            logger.warn { "No MP4 files were dropped." }
        }
        else {
            logger.info { "Dropped MP4 files: ${files.joinToString { it.name }}" }
        }

        for (file in files) {
            val resource = VideoResource(file.path, currentFolder.value)

            val addResourceCommand = AddResourceCommand(resourceManager, resource)
            commandManager.execute(addResourceCommand)
        }

        return true
    }

}