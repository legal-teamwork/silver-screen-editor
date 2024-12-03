package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.resources.Dimens
import java.awt.Cursor

class RowWindowBlock(
    override val weight: Float,
    private val blocks: List<WindowBlock>,
) : AbstractWindowBlock() {
    override val content: @Composable DimensionsScope.() -> Unit
        get() = {
            Box(Modifier.fillMaxSize()) {
                Row {
                    for ((index, block) in blocks.withIndex()) {
                        val maxWidth = width - Dimens.DIVIDER_SIZE * (blocks.size - 1)
                        val blockWidth = calculateDimension(maxWidth, block, blocks)
                        val blockHeight = height
                        val dimensionsScope = DimensionsScope(
                            blockWidth + block.deltaWidthState.value, blockHeight + block.deltaHeightState.value
                        )

                        Box(Modifier.size(dimensionsScope.width, dimensionsScope.height)) {
                            block.content.invoke(dimensionsScope)
                        }

                        if (index != blocks.lastIndex) divider(index, Dimens.DIVIDER_SIZE, height)
                    }
                }
            }
        }

    @Composable
    private fun divider(index: Int, width: Dp, height: Dp) = Box(
        Modifier
            .size(width, height)
            .pointerHoverIcon(PointerIcon.Hand)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    val currSize = blocks[index].deltaWidthState.value + dragAmount.x.dp
                    val nextSize = blocks[index + 1].deltaWidthState.value - dragAmount.x.dp
                    blocks[index].deltaWidthState.component2().invoke(currSize)
                    blocks[index + 1].deltaWidthState.component2().invoke(nextSize)
                }
            })

    companion object {
        fun calculateDimension(
            maxDimension: Dp, block: WindowBlock, blocks: List<WindowBlock>
        ) = maxDimension * block.weight / blocks.sumOf { it.weight.toDouble() }.toFloat()
    }
}