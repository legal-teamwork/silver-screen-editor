package org.legalteamwork.silverscreen.command.edit

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.ResourceOnTrack
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.rm.resource.VideoResource

class AddResourceToTrackCommand(
    private val resource: VideoResource,
    private val track: VideoTrack,
    private val position: Int
) : CommandUndoSupport {
    override val title: String = "Add video block"
    override val description: String =
        "Add resource ${resource.title} to the timeline"
    private val logger = KotlinLogging.logger {}
    private var activityResult: ResourceOnTrack? = null

    override fun execute() {
        logger.info { "Adding resource to timeline" }

        activityResult = track.addResource(resource, position)
    }

    override fun undo() {
        logger.info { "UNDO: Adding resource to timeline" }

        val result = activityResult

        if (result != null) {
            track.removeResource(result)
        } else {
            throw Exception("Invalid command manager algorithm")
        }
    }
}