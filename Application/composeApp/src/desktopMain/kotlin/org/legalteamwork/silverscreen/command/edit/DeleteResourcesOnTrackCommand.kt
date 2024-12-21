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

    // Позиция и размер удаленных ресурсов
    private var deletedResourcesInfo =
        mutableListOf<DeletedResourceInfo>()

    init {
        for (id in highlightedResources) {
            val index = track.resourcesOnTrack.indexOfFirst { it.id == id }
            if (index != -1) {
                deletedResourcesInfo.add(
                    DeletedResourceInfo(
                        id,
                        track.resourcesOnTrack[index].position,
                        track.resourcesOnTrack[index].framesCount
                    )
                )

            }
        }
    }

    override fun execute() {
        logger.info { "Deleting highlighted resources" }

        for (info in deletedResourcesInfo) {
            val index = track.resourcesOnTrack.indexOfFirst { it.id == info.id }
            if (index != -1) {
                track.resourcesOnTrack[index].position = -1
                track.resourcesOnTrack[index].framesCount = 0
            }
        }
    }

    override fun undo() {
        logger.info { "UNDO: Deleting highlighted resources" }

        for (info in deletedResourcesInfo) {
            val index = track.resourcesOnTrack.indexOfFirst { it.id == info.id }
            if (index != -1) {
                track.resourcesOnTrack[index].position = info.position
                track.resourcesOnTrack[index].framesCount = info.framesCount

            }
        }
    }

    data class DeletedResourceInfo(
        val id: Int,
        val position: Int,
        val framesCount: Int
    )
}