package org.legalteamwork.silverscreen.re

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.save.Project

/**
 * Класс-объект ползунка.
 */
object Slider {
    private var markerPosition by mutableStateOf(0)
    private var scrollOffset by mutableStateOf(0)

    fun getPosition() = markerPosition
    fun getOffset() = scrollOffset

    fun updatePosition(currentTimestamp: Long) {
        markerPosition = (currentTimestamp * Project.fps * DpInFrame / 1000).toInt()
    }
    /**
     * Устанавливает горизонтальное смещение от прокрутки.
     */
    fun updateScrollOffset(scrollValue: Int) {
        scrollOffset = scrollValue
    }
}
