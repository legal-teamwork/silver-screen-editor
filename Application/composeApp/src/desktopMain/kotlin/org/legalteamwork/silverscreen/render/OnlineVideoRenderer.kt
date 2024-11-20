package org.legalteamwork.silverscreen.render

import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.resource.ResourceFrame
import org.legalteamwork.silverscreen.ve.VideoEditorTimeState

object OnlineVideoRenderer {

    fun getVideoFrame(timestamp: Long): ResourceFrame? {
        val timeState = VideoEditorTimeState(timestamp)

        if (timeState.resourceOnTrack != null) {
            val videoResource = VideoEditor.getVideoResources()[timeState.resourceOnTrack.id]
            val videoResourceTimestamp = timeState.resourceOnTrackTimestamp

            return videoResource.getFrameByTimestamp(videoResourceTimestamp)
        } else {
            return null
        }
    }

}