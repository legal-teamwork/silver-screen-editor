package org.legalteamwork.silverscreen.command.edit

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.ResourceOnTrack
import org.legalteamwork.silverscreen.rm.window.effects.VideoFilter

class AddFilterToResource(
    private val videoFilter: VideoFilter,
    private val resourceOnTrack: ResourceOnTrack,
) : CommandUndoSupport {
    private val logger = KotlinLogging.logger { }
    override val title: String = "Add video filter"
    override val description: String =
        "Add ${videoFilter.videoEffect.title} video filter to the ${resourceOnTrack.id} resource on track"

    override fun execute() {
        logger.info { "Add ${videoFilter.videoEffect.title} video filter to the ${resourceOnTrack.id} resource on track" }
        resourceOnTrack.addFilter(videoFilter)
    }

    override fun undo() {
        logger.info { "Remove ${videoFilter.videoEffect.title} video filter from the ${resourceOnTrack.id} resource on track" }
        resourceOnTrack.removeFilter(videoFilter)
    }

}