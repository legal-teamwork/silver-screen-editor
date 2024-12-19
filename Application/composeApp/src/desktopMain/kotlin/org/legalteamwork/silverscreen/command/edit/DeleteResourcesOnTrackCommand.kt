package org.legalteamwork.silverscreen.command.edit


import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.ResourceOnTrack
import org.legalteamwork.silverscreen.re.VideoTrack


class DeleteResourcesOnTrackCommand(
    private val track: VideoTrack,
    private val highlightedResources: List<Int>
) : CommandUndoSupport {
    override val title: String = "Delete cmd"
    override val description: String = "Delete cmd"
    private val logger = KotlinLogging.logger {}
    private var deletedResources =
        mutableListOf<ResourceOnTrack>()

    /*
    init {
        for (id in highlightedResources)
            deletedResources.add(track.resourcesOnTrack.find { it.id == id }!!)
        println(highlightedResources)
    }

     */

    init {
        // Собираем удаленные ресурсы
        for (id in highlightedResources) {
            val resource = track.resourcesOnTrack.find { it.id == id }
            if (resource != null) {
                deletedResources.add(resource)
            }
        }
        val newHighlightedResourcesList: MutableList<Int> = highlightedResources.toMutableList()
        newHighlightedResourcesList.clear()

        println(newHighlightedResourcesList)
        }

    override fun execute() {
        logger.info { "Deleting highlighted resources" }

        for (resource in deletedResources)
            track.removeResource(resource)
        track.highlightedResources.clear()
    }

    override fun undo() {
        logger.info { "UNDO: Deleting highlighted resources" }

        for (resource in deletedResources)
            track.resourcesOnTrack.add(resource)
        track.highlightedResources = highlightedResources.toMutableList()
    }
}