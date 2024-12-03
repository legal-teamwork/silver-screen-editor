package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

abstract class AbstractWindowBlock : WindowBlock {
    abstract override val weight: Float
    override var deltaWidthState: MutableState<Dp> = mutableStateOf(0.dp)
    override var deltaHeightState: MutableState<Dp> = mutableStateOf(0.dp)
}