package org.legalteamwork.silverscreen.toolbar.components;

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import org.legalteamwork.silverscreen.resources.EditingPanelTheme

@Composable
fun leftEditingTools(
    modifier: Modifier = Modifier,
    onCutClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStepBackward: () -> Unit,
    onStepForward: () -> Unit,
    onVolumeChange: (Float) -> Unit = {} // Placeholder for volume change
) {
    var volume by remember { mutableStateOf(0.5f) }

    Row(
        //modifier = Modifier.padding(start = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onStepBackward) {
            Image(
                painter = painterResource("toolbar_buttons/undo_button.svg"),
                modifier = Modifier.size(24.dp),
                contentDescription = "Step backward",
            )
        }
        IconButton(onClick = onStepForward) {
            Image(
                painter = painterResource("toolbar_buttons/redo_button.svg"),
                modifier = Modifier.size(24.dp),
                contentDescription = "Step forward",
            )
        }

        IconButton(onClick = onCutClick) {
            Image(
                painter = painterResource("toolbar_buttons/cut_button.svg"),
                modifier = Modifier.size(20.dp),
                contentDescription = "Cut"
            )
        }

        IconButton(onClick = onDeleteClick) {
            Image(
                painter = painterResource("toolbar_buttons/delete_button.svg"),
                modifier = Modifier.size(20.dp),
                contentDescription = "Delete"
            )
        }
        /** Слайдер регулировки громкости
        Slider(
            value = volume,
            onValueChange = {
                volume = it
                onVolumeChange(it)
            },
            modifier = Modifier.widthIn(min = 50.dp, max = 150.dp),
            colors = SliderDefaults.colors(
                thumbColor = EditingPanelTheme.SLIDER_COLOR,
                activeTrackColor = EditingPanelTheme.SLIDER_COLOR
            )
        ) */
    }
}
