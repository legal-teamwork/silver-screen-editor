package org.legalteamwork.silverscreen.command

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.Resource

class AddResource(
    private val resourceManager: ResourceManager,
    private val resource: Resource,
    private val folder: FolderResource = resourceManager.videoResources.value
) : CommandUndoSupport {
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.info { "Adding resource '${resource.title}' to the ${resourceManager.getRelativePath(folder)} folder" }

        folder.resources.add(resource)
    }

    override fun undo() {
        logger.info { "Removing resource '${resource.title}' from the ${resourceManager.getRelativePath(folder)} folder" }

        folder.resources.remove(resource)
    }
}