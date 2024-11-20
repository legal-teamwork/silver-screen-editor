package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

@Suppress("ktlint:standard:function-naming")
@Composable
fun Slider() {
    var markerPosition by remember { mutableStateOf(0) }

    Box(
        modifier =
            Modifier
                .offset { IntOffset(markerPosition, 0) }
                .fillMaxHeight()
                .width(3.dp)
                .background(color = Color.White, RoundedCornerShape(3.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            markerPosition = max(0, markerPosition + dragAmount.x.roundToInt())
                        },
                    )
                },
    )
}
