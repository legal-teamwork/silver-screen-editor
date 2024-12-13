package org.legalteamwork.silverscreen.toolbar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.Image
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
import java.util.*

@Composable
fun centerPlaybackControls(
    modifier: Modifier = Modifier,
    currentTime: Long,
    totalDuration: Long,
    onPlayPauseClick: () -> Unit,
    onRewindBackwardsClick: () -> Unit,
    onRewindForwardClick: () -> Unit,
    onStopClick: () -> Unit,
    onSeekToStartClick: () -> Unit,
    onSeekToEndClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onSeekToStartClick) {
            Image(
                painter = painterResource("toolbar_buttons/left_start.svg"),
                contentDescription = "Seek to start"
            )
        }
        IconButton(onClick = onRewindBackwardsClick) {
            Image(
                painter = painterResource("toolbar_buttons/rewind_backwards_button.svg"),
                contentDescription = "Rewind backwards"
            )
        }
        IconButton(onClick = onPlayPauseClick) {
            Image(
                painter = painterResource("toolbar_buttons/play_button.svg"),
                contentDescription = "Play"
                )
        }
        IconButton(onClick = onStopClick) {
            Image(
                painter = painterResource("toolbar_buttons/stop_button.svg"),
                contentDescription = "Stop"
            )
        }
        IconButton(onClick = onRewindForwardClick) {
            Image(
                painter = painterResource("toolbar_buttons/rewind_forward_button.svg"),
                contentDescription = "Rewind forward"
            )
        }
        IconButton(onClick = onSeekToEndClick) {
            Image(
                painter = painterResource("toolbar_buttons/right_end.svg"),
                contentDescription = "Seek to end"
            )
        }
        Text(
            text = "${formatTime(currentTime)} / ${formatTime(totalDuration)}",
            color = EditingPanelTheme.TOOL_BUTTONS_CONTENT_COLOR
        )
    }
}

fun formatTime(time: Long): String {
    val minutes = time / 1000 / 60
    val seconds = time / 1000 % 60
    val milliseconds = time % 1000
    return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
}