package org.legalteamwork.silverscreen.re

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.legalteamwork.silverscreen.resources.Dimens

/**
 * Класс-объект ползунка.
 */
object Slider {
    private var markerPosition by mutableStateOf(0)
    private var scrollOffset by mutableStateOf(0)

    fun getPosition() = markerPosition
    fun getOffset() = scrollOffset

    fun updatePosition(currentTimestamp: Long) {
        markerPosition = (currentTimestamp * Dimens.FRAME_RATE * DpInFrame / 1000).toInt()
    }

    /**
     * Устанавливает горизонтальное смещение от прокрутки.
     */
    fun updateScrollOffset(scrollValue: Int) {
        scrollOffset = scrollValue
    }
}
