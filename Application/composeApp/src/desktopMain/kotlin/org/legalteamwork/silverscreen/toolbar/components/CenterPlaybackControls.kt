package org.legalteamwork.silverscreen.toolbar.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import org.legalteamwork.silverscreen.vp.VideoPanel.playbackManager
import java.util.*

@Composable
fun centerPlaybackControls(
    modifier: Modifier = Modifier,
    currentTimestamp: Long,
    totalDuration: Long,
    onPlayPauseClick: () -> Unit,
    onRewindBackwardsClick: () -> Unit,
    onRewindForwardClick: () -> Unit,
    onStopClick: () -> Unit,
    onSeekToStartClick: () -> Unit,
    onSeekToEndClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 400.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onSeekToStartClick) {
            Image(
                painter = painterResource("toolbar_buttons/left_start.svg"),
                modifier = Modifier.size(25.dp),
                contentDescription = "Seek to start"
            )
        }
        IconButton(onClick = onRewindBackwardsClick) {
            Image(
                painter = painterResource("toolbar_buttons/rewind_backwards_button.svg"),
                modifier = Modifier.size(25.dp),
                contentDescription = "Rewind backwards"
            )
        }
        IconButton(onClick = onPlayPauseClick) {
            if (!playbackManager.isPlaying.value) {
                Image(
                    painter = painterResource("toolbar_buttons/play_button.svg"),
                    modifier = Modifier.size(25.dp),
                    contentDescription = "Play"
                )
            }
            else {
                Image(
                    painter = painterResource("toolbar_buttons/pause_button.svg"),
                    modifier = Modifier.size(19.dp),
                    contentDescription = "Pause",
                )
            }
        }
        IconButton(onClick = onStopClick) {
            Image(
                painter = painterResource("toolbar_buttons/stop_button.svg"),
                modifier = Modifier.size(25.dp),
                contentDescription = "Stop"
            )
        }
        IconButton(onClick = onRewindForwardClick) {
            Image(
                painter = painterResource("toolbar_buttons/rewind_forward_button.svg"),
                modifier = Modifier.size(25.dp),
                contentDescription = "Rewind forward"
            )
        }
        IconButton(onClick = onSeekToEndClick) {
            Image(
                painter = painterResource("toolbar_buttons/right_end.svg"),
                modifier = Modifier.size(25.dp),
                contentDescription = "Seek to end"
            )
        }
        Text(
            text = " ${formatTime(currentTimestamp)} / ${formatTime(totalDuration)} ",
            color = EditingPanelTheme.TOOL_BUTTONS_CONTENT_COLOR
        )
    }
}

private fun formatTime(elapsedTime: Long): String {
    val hours = (elapsedTime / 3600000) % 60
    val minutes = (elapsedTime / 60000) % 60
    val seconds = (elapsedTime / 1000) % 60
    val milliseconds = (elapsedTime % 1000) / 10

    return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
}