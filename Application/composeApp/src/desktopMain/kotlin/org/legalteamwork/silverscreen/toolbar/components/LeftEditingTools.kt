package org.legalteamwork.silverscreen.toolbar.components;

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.legalteamwork.silverscreen.resources.EditingPanelTheme

@Composable
fun LeftEditingTools(
    modifier: Modifier = Modifier,
    onCutClick: () -> Unit,
    onVolumeChange: (Float) -> Unit = {} // Placeholder for volume change
) {
    var volume by remember { mutableStateOf(0.5f) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCutClick) {
            Icon(
                imageVector = Icons.Filled.ContentCut,
                contentDescription = "Cut",
                tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR
            )
        }
        // Placeholder for volume control (replace with your SVG icons and logic)
        Slider(
            value = volume,
            onValueChange = {
                volume = it
                onVolumeChange(it)
            },
            modifier = Modifier,
            colors = SliderDefaults.colors(
                thumbColor = EditingPanelTheme.TOOLBAR_SLIDER_COLOR,
                activeTrackColor = EditingPanelTheme.TOOLBAR_SLIDER_COLOR
            )
        )
    }
}
