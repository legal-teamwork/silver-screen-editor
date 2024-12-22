package org.legalteamwork.silverscreen.toolbar.components;

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import org.legalteamwork.silverscreen.resources.EditingPanelTheme

@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 22.5f..75f,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 1.dp,
    thumbSize: Dp = 2.dp,
    trackColor: Color = EditingPanelTheme.ZOOM_SLIDER_COLOR,
    activeTrackColor: Color = EditingPanelTheme.ZOOM_SLIDER_COLOR_BORDER,
    thumbColor: Color = EditingPanelTheme.ZOOM_SLIDER_COLOR_THUMB,
    borderColor: Color = EditingPanelTheme.ZOOM_SLIDER_COLOR_BORDER
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(thumbSize)
    ) {
        val width = constraints.maxWidth.toFloat()
        val thumbRadius = 5.dp
        val trackHeightPx = trackHeight.value

        val normalizedValue = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)
        var thumbOffsetX by remember { mutableStateOf(normalizedValue * width) }

        LaunchedEffect(value) {
            thumbOffsetX = normalizedValue * width
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        thumbOffsetX = (thumbOffsetX + dragAmount.x).coerceIn(0f, width)
                        val newValue = (thumbOffsetX / width) * (valueRange.endInclusive - valueRange.start) + valueRange.start
                        onValueChange(newValue.coerceIn(valueRange.start, valueRange.endInclusive))
                    }
                }
        ) {
            drawRect(
                color = trackColor,
                size = androidx.compose.ui.geometry.Size(width, trackHeightPx),
                topLeft = Offset(0f, (size.height - trackHeightPx) / 2)
            )

            drawRect(
                color = activeTrackColor,
                size = androidx.compose.ui.geometry.Size(thumbOffsetX, trackHeightPx),
                topLeft = Offset(0f, (size.height - trackHeightPx) / 2)
            )

            // Рисуем внутренний круг
            drawCircle(
                color = thumbColor, // Цвет внутреннего круга
                radius = thumbRadius.value * 0.9f, // Радиус немного меньше, чтобы обводка была видна
                center = Offset(thumbOffsetX, size.height / 2)
            )

// Рисуем внешнюю границу круга
            drawCircle(
                color = borderColor, // Цвет границы
                radius = thumbRadius.value, // Полный радиус
                center = Offset(thumbOffsetX, size.height / 2),
                style = Stroke(width = 2.dp.toPx()) // Толщина границы
            )

        }
    }
}

@Composable
fun rightEditingTools(
    modifier: Modifier = Modifier,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    zoomLevel: Float,
    onZoomLevelChange: (Float) -> Unit,
    //onRenderClick: () -> Unit
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

        /*
        Slider (
            value = zoomLevel,
            onValueChange = onZoomLevelChange,
            valueRange = 22.5f..75f,
            modifier = Modifier.width(150.dp),
            colors = SliderDefaults.colors(
                thumbColor = EditingPanelTheme.ZOOM_SLIDER_COLOR,
                activeTrackColor = EditingPanelTheme.ZOOM_SLIDER_COLOR
            ),
        )

         */

        CustomSlider(
            value = zoomLevel,
            onValueChange = onZoomLevelChange,
            modifier = Modifier.width(150.dp),
            thumbSize = 12.dp, // Уменьшенный размер ползунка
            trackHeight = 2.dp,
        )

        IconButton(onClick = onZoomIn) {
            Image(
                painter = painterResource("toolbar_buttons/zoom_up_button.svg"),
                modifier = Modifier.size(15.dp),
                contentDescription = "Zoom in"
            )
        }

        /*
        Button(
            onClick = onRenderClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF272C40)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Render", color = Color.White)
        }

         */

        //Spacer(modifier = Modifier.width(8.dp))
    }
}
