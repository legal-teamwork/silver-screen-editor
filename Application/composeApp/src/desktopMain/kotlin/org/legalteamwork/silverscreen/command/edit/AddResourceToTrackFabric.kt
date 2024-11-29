package org.legalteamwork.silverscreen.command.edit

import org.legalteamwork.silverscreen.command.CommandUndoSupport
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.resource.VideoResource

object AddResourceToTrackFabric {

    fun makeCommand(resource: Resource, track: VideoEditor.VideoTrack, position: Int): CommandUndoSupport? =
        when (resource) {
            is VideoResource -> AddResourceToTrackCommand(resource, track, position)
            else -> null
        }
}