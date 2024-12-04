package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BlockWithDimensions(
    val block: WindowBlock,
    val weight: Float,
    var initiationWidth: Dp = Dp.Unspecified,
    var initiationHeight: Dp = Dp.Unspecified,
    val deltaWidth: MutableState<Dp> = mutableStateOf(0.dp),
    val deltaHeight: MutableState<Dp> = mutableStateOf(0.dp),
) {
    val minWidth: Dp = block.minWidth
    val minHeight: Dp = block.minHeight
    val maxWidth: Dp = block.maxWidth
    val maxHeight: Dp = block.maxHeight

    val width: Dp
        get() = initiationWidth + deltaWidth.value
    val height: Dp
        get() = initiationHeight + deltaHeight.value
}