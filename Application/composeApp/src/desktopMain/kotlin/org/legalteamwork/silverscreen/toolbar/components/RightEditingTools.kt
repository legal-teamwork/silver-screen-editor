package org.legalteamwork.silverscreen.toolbar.components;

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.re.Slider
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.vp.VideoPanel


@Composable
fun RightEditingTools(
    modifier: Modifier = Modifier,
    onStepBackward: () -> Unit = {}, // Placeholder for step backward
    onStepForward: () -> Unit = {}, // Placeholder for step forward
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onSaveClick: () -> Unit
) {
    var zoomLevel by remember { mutableStateOf(1f) } // Placeholder zoom level

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onStepBackward) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Step backward", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }
        IconButton(onClick = onStepForward) {
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Step forward", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }

        IconButton(onClick = onZoomOut) {
            Icon(imageVector = Icons.Filled.ZoomOut, contentDescription = "Zoom out", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }

        Slider(
            value = zoomLevel,
            onValueChange = { zoomLevel = it }, // Placeholder, connect to actual zoom logic
            modifier = Modifier.width(100.dp), // Adjust width as needed
            colors = SliderDefaults.colors(
                thumbColor = EditingPanelTheme.TOOLBAR_SLIDER_COLOR,
                activeTrackColor = EditingPanelTheme.TOOLBAR_SLIDER_COLOR
            )
        )

        IconButton(onClick = onZoomIn) {
            Icon(imageVector = Icons.Filled.ZoomIn, contentDescription = "Zoom in", tint = EditingPanelTheme.TOOLBAR_ICONS_COLOR)
        }


        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text("Save", color = Color.White)
        }
    }
}
