package org.legalteamwork.silverscreen.toolbar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.legalteamwork.silverscreen.vp.VideoPanel
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CenterPlaybackControls(
    modifier: Modifier = Modifier,
    currentTime: Long,
    totalDuration: Long
) {
    val playbackManager = VideoPanel.playbackManager
    val isPlaying = playbackManager.isPlaying.collectAsState()
    val timeFormat = SimpleDateFormat("mm:ss:SSS", Locale.getDefault())

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { playbackManager.seekToStart() }) {
            Icon(imageVector = Icons.Filled.SkipToPrevious, contentDescription = "Skip to start", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }
        IconButton(onClick = { playbackManager.rewindBackwards() }) {
            Icon(imageVector = Icons.Filled.FastRewind, contentDescription = "Rewind backwards", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }

        IconButton(onClick = {
            if (isPlaying.value) playbackManager.pause() else playbackManager.play()
        }) {
            Icon(
                imageVector = if (isPlaying.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = if (isPlaying.value) "Pause" else "Play",
                tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR
            )
        }

        IconButton(onClick = { playbackManager.stop() }) {
            Icon(imageVector = Icons.Filled.Stop, contentDescription = "Stop", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }
        IconButton(onClick = { playbackManager.rewindForward() }) {
            Icon(imageVector = Icons.Filled.FastForward, contentDescription = "Rewind forwards", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }
        IconButton(onClick = { playbackManager.seekToEnd() }) {
            Icon(imageVector = Icons.Filled.SkipToNext, contentDescription = "Skip to end", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }

        Text(
            text = "${timeFormat.format(Date(currentTime))} / ${timeFormat.format(Date(totalDuration))}",
            color = EditingPanelTheme.TOOLBAR_TEXT_COLOR
        )
    }
}