package org.legalteamwork.silverscreen.command.edit

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.ResourceOnTrack
import org.legalteamwork.silverscreen.re.VideoTrack

class MoveResourceOnTrackCommand(
    private val resourceOnTrack: ResourceOnTrack,
    private val track: VideoTrack,
    private val positionTo: Int
) : CommandUndoSupport {
    override val title: String = "Move video block"
    override val description: String =
        "Move resource ${resourceOnTrack.id} on the timeline to the $positionTo position"
    private val logger = KotlinLogging.logger {}
    private var trackChanges: List<ResourceChange>? = null

    private fun prepareChanges(): List<Pair<Int, Int>> {
        var leftBorder = positionTo
        var rightBorder = leftBorder + resourceOnTrack.framesCount - 1
        val changes = mutableListOf<Pair<Int, Int>>()

        for (resource in track.resourcesOnTrack.sortedBy { it.position }) {
            if (resource.id == resourceOnTrack.id) {
                continue
            }

            if (leftBorder in resource.getLeftBorder() + 1..resource.getRightBorder()) {
                leftBorder = resource.getRightBorder() + 1
                rightBorder = leftBorder + resourceOnTrack.framesCount - 1
            }
            else if (resource.getLeftBorder() in leftBorder..rightBorder) {
                changes.add(Pair(resource.id, rightBorder + 1))
                rightBorder += resource.framesCount
            }
        }
        changes.add(Pair(resourceOnTrack.id, leftBorder))

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
    val resourceOnTrack: ResourceOnTrack,
    val positionFrom: Int,
    val positionTo: Int,
)