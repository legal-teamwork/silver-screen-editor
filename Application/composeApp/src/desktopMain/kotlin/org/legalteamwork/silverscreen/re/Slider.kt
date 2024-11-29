package org.legalteamwork.silverscreen.re

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
import org.legalteamwork.silverscreen.resources.Dimens
import kotlin.math.roundToInt
import org.legalteamwork.silverscreen.vp.VideoPanel

/**
 * Класс-объект ползунка.
 */
object Slider {

    var markerPosition by mutableStateOf(0)
    private var scrollOffset by mutableStateOf(0)

    fun updatePosition(currentTimestamp: Long) {
        markerPosition = (currentTimestamp * Dimens.FRAME_RATE * DpInFrame / 1000).roundToInt()
    }

    /**
     * Устанавливает горизонтальное смещение от прокрутки.
     */
    fun updateScrollOffset(scrollValue: Int) {
        scrollOffset = scrollValue
    }

    @Suppress("ktlint:standard:function-naming")
    @Composable
    fun compose() {
        Box(
            modifier =
            Modifier
                .offset { IntOffset(markerPosition - scrollOffset, 0) } // Учитываем скролл
                .fillMaxHeight()
                .width(3.dp)
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
