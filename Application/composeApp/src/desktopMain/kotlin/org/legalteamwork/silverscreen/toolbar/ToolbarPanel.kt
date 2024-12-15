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
import org.legalteamwork.silverscreen.toolbar.components.centerPlaybackControls
import org.legalteamwork.silverscreen.toolbar.components.leftEditingTools
import org.legalteamwork.silverscreen.toolbar.components.rightEditingTools
import org.legalteamwork.silverscreen.re.Slider
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.command.edit.CutResourceOnTrackCommand
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.vp.VideoPanel
import org.legalteamwork.silverscreen.vp.VideoPanel.playbackManager

@Composable
fun AppScope.ToolbarPanel(modifier: Modifier = Modifier) {
    val totalProjectDuration = remember(VideoEditor.getResourcesOnTrack()) {
        VideoEditor.getResourcesOnTrack().maxOfOrNull { it.getRightBorder() }?.toLong() ?: 0L
    }

    Row(
        modifier = modifier.fillMaxWidth().background(EditingPanelTheme.EDITING_PANEL_BACKGROUND).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        leftEditingTools(
            onCutClick = {
                if (VideoPanel.playbackManager.isPlaying.value)
                    VideoPanel.playbackManager.pause()
                val cutResourceOnTrackCommand =
                    CutResourceOnTrackCommand(VideoTrack, Slider.getPosition(), 0)
                commandManager.execute(cutResourceOnTrackCommand)
            },
        )

        centerPlaybackControls(
            currentTimestamp = playbackManager.currentTimestamp.value,
            totalDuration = (totalProjectDuration * 1000 / Dimens.FRAME_RATE).toLong(),
            onPlayPauseClick = {
                playbackManager.playOrPause()
            },
            onRewindBackwardsClick = {
                VideoPanel.playbackManager.seek(-10_000)
            },
            onRewindForwardClick = {
                VideoPanel.playbackManager.seek(10_000)
            },
            onStopClick = {
                VideoPanel.playbackManager.stop()
            },
            onSeekToStartClick = {
                //VideoPanel.playbackManager.seekToStart()
                VideoPanel.playbackManager.pause()

            },
            onSeekToEndClick = {
                //VideoPanel.playbackManager.seekToEnd()
                VideoPanel.playbackManager.pause()
            },
        )

        rightEditingTools(
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
                VideoTrack.updateResourcesOnTrack()
            },
            onZoomOut = {
                org.legalteamwork.silverscreen.re.DpInFrame -= 0.25f
                if (org.legalteamwork.silverscreen.re.DpInFrame < 0.75f) {
                    org.legalteamwork.silverscreen.re.DpInFrame = 0.75f
                }
                VideoTrack.updateResourcesOnTrack()
            },
            onSaveClick = {
                // Placeholder for save functionality
            }
        )
    }
}