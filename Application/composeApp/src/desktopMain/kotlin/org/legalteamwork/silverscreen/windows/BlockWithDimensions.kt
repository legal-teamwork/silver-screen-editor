package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    fun getWidth() = initiationWidth + deltaWidth.value
    fun getHeight() = initiationHeight + deltaHeight.value

    private fun getMinDeltaWidth() = minWidth - initiationWidth
    private fun getMaxDeltaWidth() = maxWidth - initiationWidth
    private fun getDeltaWidthRange() = getMinDeltaWidth()..getMaxDeltaWidth()

    private fun getMinDeltaHeight() = minHeight - initiationHeight
    private fun getMaxDeltaHeight() = maxHeight - initiationHeight
    private fun getDeltaHeightRange() = getMinDeltaHeight()..getMaxDeltaHeight()

    fun addToDeltaWidth(addition: Dp): Pair<Dp, Dp> {
        var delegate by deltaWidth
        val delegateBefore = delegate
        val delegateAfter = (delegate + addition).coerceIn(getDeltaWidthRange())
        val added = delegateAfter - delegateBefore
        val ignored = addition - added
        delegate = delegateAfter

        return added to ignored
    }

    fun addToDeltaHeight(addition: Dp): Pair<Dp, Dp> {
        var delegate by deltaHeight
        val delegateBefore = delegate
        val delegateAfter = (delegate + addition).coerceIn(getDeltaHeightRange())
        val added = delegateAfter - delegateBefore
        val ignored = addition - added
        delegate = delegateAfter

        return added to ignored
    }
}