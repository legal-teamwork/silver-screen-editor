package org.legalteamwork.silverscreen.re

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.vp.VideoPanel

/**
 * Класс-объект ползунка.
 */
object Slider {
    private var markerPosition by mutableStateOf(0)
    private var scrollOffset by mutableStateOf(0)

    fun updatePosition(currentTimestamp: Long) {
        markerPosition = (currentTimestamp * Dimens.FRAME_RATE * DpInFrame / 1000).toInt()
    }

    fun getPosition() = markerPosition

    /**
     * Устанавливает горизонтальное смещение от прокрутки.
     */
    fun updateScrollOffset(scrollValue: Int) {
        scrollOffset = scrollValue
    }

    @Suppress("ktlint:standard:function-naming")
    @Composable
    fun compose(panelHeight: Dp) {
        Box(
            modifier =
                Modifier
                    .offset { IntOffset(markerPosition - scrollOffset, 0) }
                    .width(2.dp)
                    .height(panelHeight)
                    .padding(top = 50.dp)
                    .background(color = Color.White, RoundedCornerShape(3.dp))
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                if (!VideoPanel.playbackManager.isPlaying.value) {
                                    change.consume()
                                    val delta = (dragAmount.x * 1000 / (Dimens.FRAME_RATE * DpInFrame)).toLong()
                                    VideoPanel.playbackManager.seek(delta)
                                }
                            },
                        )
                    },
        )
    }
}
