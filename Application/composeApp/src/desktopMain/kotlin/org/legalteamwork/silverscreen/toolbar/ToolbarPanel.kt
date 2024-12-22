package org.legalteamwork.silverscreen.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.legalteamwork.silverscreen.command.edit.CutResourceOnTrackCommand
import org.legalteamwork.silverscreen.command.edit.DeleteResourcesOnTrackCommand
import org.legalteamwork.silverscreen.re.*
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.vp.VideoPanel
import org.legalteamwork.silverscreen.vp.VideoPanel.playbackManager

@Composable
fun AppScope.ToolbarPanel(modifier: Modifier = Modifier) {

    var zoomLevel by remember { mutableStateOf(org.legalteamwork.silverscreen.re.DpPerSecond) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                EditingPanelTheme.TOOLBOX_PANEL_BACKGROUND,
                RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        leftEditingTools(
            onCutClick = {
                if (VideoPanel.playbackManager.isPlaying.value)
                    VideoPanel.playbackManager.pause()
                val position = Slider.getPosition()
                val index = VideoTrack.resourcesOnTrack.indexOfFirst{ it.isPosInside(position) }
                if (index != -1) {
                    val cutResourceOnTrackCommand = CutResourceOnTrackCommand(VideoTrack, position, index)
                    commandManager.execute(cutResourceOnTrackCommand)
                }
            },

            onDeleteClick = {
                val highlightedResources = VideoEditor.getHighlightedResources()
                if (highlightedResources.isNotEmpty()) {
                    commandManager.execute(DeleteResourcesOnTrackCommand(VideoTrack, highlightedResources))
                    VideoEditor.resetHighlighting()
                }
            },

            onStepBackward = {
                commandManager.undo()
                // step backward logic here
            },
            onStepForward = {
                commandManager.redo()
                // step forward logic here
            },
        )

        centerPlaybackControls(
            currentTimestamp = playbackManager.currentTimestamp.value,
            totalDuration = playbackManager.getTotalDuration(),
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
                VideoPanel.playbackManager.seekToStart()
            },
            onSeekToEndClick = {
                VideoPanel.playbackManager.seekToEnd()
            }
        )

        rightEditingTools(

            onZoomIn = {
                zoomLevel = (zoomLevel + 7.5f).coerceAtMost(75f) // Увеличиваем zoomLevel
                org.legalteamwork.silverscreen.re.DpPerSecond = zoomLevel
                VideoTrack.updateResourcesOnTrack()
            },
            onZoomOut = {
                zoomLevel = (zoomLevel - 7.5f).coerceAtLeast(22.5f) // Уменьшаем zoomLevel
                org.legalteamwork.silverscreen.re.DpPerSecond = zoomLevel
                VideoTrack.updateResourcesOnTrack()
            },

            zoomLevel = zoomLevel,
            onZoomLevelChange = { newZoom ->
                zoomLevel = newZoom.coerceIn(22.5f, 75f)
                org.legalteamwork.silverscreen.re.DpPerSecond = zoomLevel
                VideoTrack.updateResourcesOnTrack()
            },

            /*
            onRenderClick = {
                // Placeholder for save functionality
            }

             */
        )
    }
}