package org.legalteamwork.silverscreen.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.toolbar.components.CenterPlaybackControls
import org.legalteamwork.silverscreen.toolbar.components.LeftEditingTools
import org.legalteamwork.silverscreen.toolbar.components.RightEditingTools
import org.legalteamwork.silverscreen.re.Slider
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.command.edit.CutResourceOnTrackCommand
import org.legalteamwork.silverscreen.vp.VideoPanel

@Composable
fun AppScope.ToolbarPanel(modifier: Modifier = Modifier) {
    val totalProjectDuration = remember(VideoEditor.getResourcesOnTrack()) {
        VideoEditor.getResourcesOnTrack().maxOfOrNull { it.getRightBorder() }?.toLong() ?: 0L
    }

    Row(
        modifier = modifier.fillMaxWidth().background(EditingPanelTheme.TOOLBAR_BACKGROUND_COLOR).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeftEditingTools(
            onCutClick = {
                if (VideoPanel.playbackManager.isPlaying.value)
                    VideoPanel.playbackManager.pause()
                val cutResourceOnTrackCommand =
                    CutResourceOnTrackCommand(VideoEditor.VideoTrack, Slider.getPosition())
                commandManager.execute(cutResourceOnTrackCommand)
            },
        )

        CenterPlaybackControls(
            currentTime = Slider.getPosition().toLong(),
            totalDuration = totalProjectDuration * 1000 / org.legalteamwork.silverscreen.resources.Dimens.FRAME_RATE
        )

        RightEditingTools(
            onStepBackward = {
                // step backward logic here
            },
            onStepForward = {
                // step forward logic here
            },
            onZoomIn = {
                org.legalteamwork.silverscreen.re.DpInFrame += 0.25f
                if (org.legalteamwork.silverscreen.re.DpInFrame > 2.5f) {
                    org.legalteamwork.silverscreen.re.DpInFrame = 2.5f
                }
                VideoEditor.VideoTrack.updateResourcesOnTrack()
            },
            onZoomOut = {
                org.legalteamwork.silverscreen.re.DpInFrame -= 0.25f
                if (org.legalteamwork.silverscreen.re.DpInFrame < 0.75f) {
                    org.legalteamwork.silverscreen.re.DpInFrame = 0.75f
                }
                VideoEditor.VideoTrack.updateResourcesOnTrack()
            },
            onSaveClick = {
                // Placeholder for save functionality
            }
        )
    }
}