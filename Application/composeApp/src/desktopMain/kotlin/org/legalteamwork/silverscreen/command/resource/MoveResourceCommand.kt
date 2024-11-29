package org.legalteamwork.silverscreen.command.resource

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.Resource

class MoveResourceCommand(
    private val resourceManager: ResourceManager,
    private val resource: Resource,
    private val folderFrom: FolderResource,
    private val folderTo: FolderResource
) : CommandUndoSupport {
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.info { "Add '${resource.title}' from ${resourceManager.getRelativePath(folderFrom)} to ${resourceManager.getRelativePath(folderTo)}" }

        folderFrom.resources.remove(resource)
        folderTo.resources.add(resource)
    }

    override fun undo() {
        logger.info { "UNDO: Add '${resource.title}' from ${resourceManager.getRelativePath(folderFrom)} to ${resourceManager.getRelativePath(folderTo)}" }

        folderTo.resources.remove(resource)
        folderFrom.resources.add(resource)
    }
}