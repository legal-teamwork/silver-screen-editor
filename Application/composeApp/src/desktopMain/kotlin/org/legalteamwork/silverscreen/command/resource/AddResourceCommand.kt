package org.legalteamwork.silverscreen.command.resource

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.Resource

class AddResourceCommand(
    private val resourceManager: ResourceManager,
    private val resource: Resource,
    private val folder: FolderResource = resourceManager.currentFolder.value
) : CommandUndoSupport {
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.info { "Add '${resource.title}' to ${resourceManager.getRelativePath(folder)}" }

        folder.resources.add(resource)
    }

    override fun undo() {
        logger.info { "UNDO: Add '${resource.title}' to ${resourceManager.getRelativePath(folder)}" }

        folder.resources.remove(resource)
    }
}