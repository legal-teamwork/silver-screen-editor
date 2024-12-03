package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.resources.Dimens

class ColumnWindowBlock(
    override val weight: Float,
    private val blocks: List<WindowBlock>,
) : AbstractWindowBlock() {
    override val content: @Composable DimensionsScope.() -> Unit
        get() = {
            Box(Modifier.fillMaxSize()) {
                Column {
                    for ((index, block) in blocks.withIndex()) {
                        val maxHeight = height - Dimens.DIVIDER_SIZE * (blocks.size - 1)
                        val blockWidth = width
                        val blockHeight = calculateDimension(maxHeight, block, blocks)

                        val dimensionsScope = DimensionsScope(
                            blockWidth + block.deltaWidthState.value, blockHeight + block.deltaHeightState.value
                        )

                        Box(Modifier.size(dimensionsScope.width, dimensionsScope.height)) {
                            block.content.invoke(dimensionsScope)
                        }

                        if (index != blocks.lastIndex) divider(index, width, Dimens.DIVIDER_SIZE)
                    }
                }
            }
        }

    @Composable
    private fun divider(index: Int, width: Dp, height: Dp) = Box(
        Modifier.size(width, height).pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()

                val currSize = blocks[index].deltaHeightState.value + dragAmount.y.dp
                val nextSize = blocks[index + 1].deltaHeightState.value - dragAmount.y.dp
                blocks[index].deltaHeightState.component2().invoke(currSize)
                blocks[index + 1].deltaHeightState.component2().invoke(nextSize)
            }
        })

    companion object {
        fun calculateDimension(
            maxDimension: Dp, block: WindowBlock, blocks: List<WindowBlock>
        ) = maxDimension * block.weight / blocks.sumOf { it.weight.toDouble() }.toFloat()
    }
}