package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.Dp

interface WindowBlock {
    val initialSize: Float
    val deltaWidthState: MutableState<Dp>
    val deltaHeightState: MutableState<Dp>
    val content: @Composable DimensionsScope.() -> Unit
}