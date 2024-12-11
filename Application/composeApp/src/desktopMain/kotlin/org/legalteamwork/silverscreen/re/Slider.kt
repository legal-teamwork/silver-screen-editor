package org.legalteamwork.silverscreen.re

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.vp.VideoPanel

/**
 * Класс-объект ползунка.
 */
object Slider {
    private var markerPosition by mutableStateOf(0)

    fun updatePosition(currentTimestamp: Long) {
        markerPosition = (currentTimestamp * DpPerSecond / 1000).toInt()
    }

    fun getPosition() = (markerPosition / DpInFrame).toInt()

    @Suppress("ktlint:standard:function-naming")
    @Composable
    fun compose(panelHeight: Dp) {
        val hitboxWidth = 48.dp
        val sliderWidth = 2.dp
        val circleSize = 12.dp

        Box(
            modifier = Modifier
                // Центрируем область взаимодействия относительно текущей позиции слайдера
                .offset { IntOffset(markerPosition - hitboxWidth.toPx().toInt() / 2, 0) }
                .width(hitboxWidth)
                .height(panelHeight)
                .padding(top = 50.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
//                            if (!VideoPanel.playbackManager.isPlaying.value) {
                                change.consume()
                                val delta = (dragAmount.x * 1000 / DpPerSecond).toLong()
                                VideoPanel.playbackManager.seek(delta)
//                            }
                        },
                    )
                },
        ) {
            //Кружок над слайдером
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(circleSize)
                    .background(color = Color.White, RoundedCornerShape(50))
            )
            // Внутренний слайдер, остающийся визуально неизменным
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(sliderWidth)
                    .height(panelHeight)
                    .background(color = Color.White, RoundedCornerShape(3.dp))
            )
        }
    }
}
