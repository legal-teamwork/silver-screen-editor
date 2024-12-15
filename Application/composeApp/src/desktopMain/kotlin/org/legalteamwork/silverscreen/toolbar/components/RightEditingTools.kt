package org.legalteamwork.silverscreen.toolbar.components;

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import org.legalteamwork.silverscreen.re.Slider
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.vp.VideoPanel

@Composable
fun rightEditingTools(
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
            Image(
                painter = painterResource("toolbar_buttons/arrow_back.svg"),
                contentDescription = "Step backward",
            )
        }
        IconButton(onClick = onStepForward) {
            Image(
                painter = painterResource("toolbar_buttons/arrow_forward.svg"),
                contentDescription = "Step forward",
            )
        }

        IconButton(onClick = onZoomOut) {
            Image(
                painter = painterResource("toolbar_buttons/minus.svg"),
                contentDescription = "Zoom out",
            )
        }

        Slider(
            value = zoomLevel,
            onValueChange = { zoomLevel = it }, // Placeholder, connect to actual zoom logic
            modifier = Modifier.width(100.dp), // Adjust width as needed
            colors = SliderDefaults.colors(
                thumbColor = EditingPanelTheme.SLIDER_COLOR,
                activeTrackColor = EditingPanelTheme.SLIDER_COLOR
            )
        )

        IconButton(onClick = onZoomIn) {
            Image(
                painter = painterResource("toolbar_buttons/plus.svg"),
                contentDescription = "Zoom in"
            )
        }

        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text("Save", color = Color.White)
        }
    }
}
