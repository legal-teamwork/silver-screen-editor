package org.legalteamwork.silverscreen.command.edit


import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.VideoEditor

class DeleteResourcesOnTrackCommand(
    private val track: VideoEditor.VideoTrack,
    private val highlightedResources: List<Int>
) : CommandUndoSupport {
    override val title: String = "Delete highlighted resources command"
    override val description: String = "Delete highlighted resources command"
    private val logger = KotlinLogging.logger {}
    private var deletedResources =
        mutableListOf<VideoEditor.VideoTrack.ResourceOnTrack>()
    init {
        for (id in highlightedResources)
            deletedResources.add(track.resourcesOnTrack.find { it.id == id }!!)
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