package org.legalteamwork.silverscreen.command.edit

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.VideoEditor

class MoveResourceOnTrackCommand(
    private val resourceOnTrack: VideoEditor.VideoTrack.ResourceOnTrack,
    private val track: VideoEditor.VideoTrack,
    private val positionTo: Int
) : CommandUndoSupport {

    private val logger = KotlinLogging.logger {}
    private var trackChanges: List<ResourceChange>? = null

    private fun prepareChanges(): List<Pair<Int, Int>> {
        var leftBorder = positionTo
        var rightBorder = leftBorder + resourceOnTrack.framesCount - 1
        val changes = mutableListOf<Pair<Int, Int>>()
        var fl = false
        for (resource in track.resourcesOnTrack.sortedBy { it.position }) {
            if (resource.id == resourceOnTrack.id) {
                changes.add(Pair(resourceOnTrack.id, leftBorder))
                fl = true
                continue
            }
            if (fl) {
                if (resource.getLeftBorder() in leftBorder..rightBorder) {
                    changes.add(Pair(resource.id, rightBorder + 1))
                    rightBorder += resource.framesCount
                }
            } else if (leftBorder in resource.getLeftBorder()..resource.getRightBorder()) {
                leftBorder = resource.getRightBorder() + 1
                rightBorder = leftBorder + resourceOnTrack.framesCount - 1
                fl = true
            }
        }

        return changes
    }

    override fun execute() {
        val changes = prepareChanges()
        val resourceChanges = changes.map { change ->
            val resourceOnTrack = track.resourcesOnTrack.first { it.id == change.first }
            ResourceChange(resourceOnTrack, resourceOnTrack.position, change.second)
        }
        trackChanges = resourceChanges

        logger.info { "Repositioning of video resources..." }

        for (change in resourceChanges)
            change.resourceOnTrack.updatePosition(change.positionTo)
    }

    override fun undo() {
        logger.info { "UNDO: Repositioning of video resources..." }

        val resourceChanges = trackChanges ?: throw Exception("Invalid command manager algorithm")

        logger.info { "Repositioning of video resources..." }

        for (change in resourceChanges.reversed())
            change.resourceOnTrack.updatePosition(change.positionFrom)
    }
}

private data class ResourceChange(
    val resourceOnTrack: VideoEditor.VideoTrack.ResourceOnTrack,
    val positionFrom: Int,
    val positionTo: Int,
)