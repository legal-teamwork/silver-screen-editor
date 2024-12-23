package org.legalteamwork.silverscreen.command.edit

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.DpInFrame
import org.legalteamwork.silverscreen.re.ResourceOnTrack
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.resources.Dimens

class CutResourceOnTrackCommand(
    private val track: VideoTrack,
    private val position: Int,
    private val index: Int
) : CommandUndoSupport {
    override val title: String = "Cut command"
    override val description: String = "Cut track with the $position position"
    private val logger = KotlinLogging.logger {}
    private var leftResource: ResourceOnTrack? = null
    private var rightResource: ResourceOnTrack? = null

    override fun execute() {
        logger.info { "Cutting video resource" }

        val pos = (position / DpInFrame).toInt()
        val leftSize = pos - VideoTrack.resourcesOnTrack[index].getLeftBorder()
        val rightSize = VideoTrack.resourcesOnTrack[index].getRightBorder() - pos + 1
        if (leftSize >= Dimens.MIN_SIZE_OF_RESOURCE_ON_TRACK &&
            rightSize >= Dimens.MIN_SIZE_OF_RESOURCE_ON_TRACK
        ) {
            track.resourcesOnTrack[index].framesCount = leftSize
            leftResource = track.resourcesOnTrack[index]
            rightResource = track.addResource(
                track.videoResources[index],
                pos,
                rightSize,
                track.resourcesOnTrack[index].framesSkip + leftSize,
                leftResource?.filters?.toList() ?: emptyList()
            )
        }
    }

    override fun undo() {
        logger.info { "UNDO: Cutting video resource" }

        if (leftResource != null &&
            rightResource != null
            ) {
            val index = track.resourcesOnTrack.indexOf(leftResource)
            if (index != -1) {
                track.resourcesOnTrack[index].framesCount = leftResource!!.framesCount + rightResource!!.framesCount
                track.removeResource(rightResource!!)
            }
        }
    }
}