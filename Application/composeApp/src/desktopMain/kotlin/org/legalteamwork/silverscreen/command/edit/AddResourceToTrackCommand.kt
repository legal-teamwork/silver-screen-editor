package org.legalteamwork.silverscreen.command.edit

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.rm.resource.VideoResource

class AddResourceToTrackCommand(
    private val resource: VideoResource,
    private val track: VideoEditor.VideoTrack,
    private val position: Int
) : CommandUndoSupport {

    private val logger = KotlinLogging.logger {}
    private var activityResult: VideoEditor.VideoTrack.ResourceOnTrack? = null

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