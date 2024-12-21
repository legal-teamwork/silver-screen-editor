package org.legalteamwork.silverscreen.toolbar.components;

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    zoomLevel: Float,
    onZoomLevelChange: (Float) -> Unit,
    onRenderClick: () -> Unit
) {
    val zoomLevels = listOf(22.5f, 30f, 37.5f, 45f, 52.5f, 60f, 75f)
    val currentZoomIndex = zoomLevels.indexOfFirst { it == zoomLevel }.coerceAtLeast(0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onZoomOut) {
            Image(
                painter = painterResource("toolbar_buttons/zoom_down_button.svg"),
                modifier = Modifier.size(15.dp),
                contentDescription = "Zoom out",
            )
        }

        Slider (
            value = zoomLevel,
            onValueChange = onZoomLevelChange,
            valueRange = 22.5f..75f,
            modifier = Modifier.width(100.dp),
            colors = SliderDefaults.colors(
                thumbColor = EditingPanelTheme.ZOOM_SLIDER_COLOR,
                activeTrackColor = EditingPanelTheme.ZOOM_SLIDER_COLOR
            )
        )

        IconButton(onClick = onZoomIn) {
            Image(
                painter = painterResource("toolbar_buttons/zoom_up_button.svg"),
                modifier = Modifier.size(15.dp),
                contentDescription = "Zoom in"
            )
        }

        Button(
            onClick = onRenderClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF272C40)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Render", color = Color.White)
        }

        //Spacer(modifier = Modifier.width(8.dp))
    }
}
