package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.resources.Dimens

class BlockListBlock(
    override val weight: Float,
    val blocks: List<WindowBlock>,
    val blockListType: BlockListType
) : AbstractWindowBlock() {
    override val content: @Composable DimensionsScope.() -> Unit
        get() = {
            Box(Modifier.fillMaxSize()) {
                when (blockListType) {
                    BlockListType.ROW -> Row {
                        for ((index, block) in blocks.withIndex()) {
                            val maxWidth = width - Dimens.DIVIDER_SIZE * (blocks.size - 1)
                            val blockWidth = calculateDimension(maxWidth, block, blocks)
                            val blockHeight = height
                            val dimensionsScope = DimensionsScope(
                                blockWidth + block.deltaWidthState.value,
                                blockHeight + block.deltaHeightState.value
                            )

                            Box(Modifier.size(dimensionsScope.width, dimensionsScope.height)) {
                                block.content.invoke(dimensionsScope)
                            }

                            if (index != blocks.lastIndex) divider(index, Dimens.DIVIDER_SIZE, height)
                        }
                    }

                    BlockListType.COLUMN -> Column {
                        for ((index, block) in blocks.withIndex()) {
                            val maxHeight = height - Dimens.DIVIDER_SIZE * (blocks.size - 1)
                            val blockWidth = width
                            val blockHeight = calculateDimension(maxHeight, block, blocks)

                            val dimensionsScope = DimensionsScope(
                                blockWidth + block.deltaWidthState.value,
                                blockHeight + block.deltaHeightState.value
                            )

                            Box(Modifier.size(dimensionsScope.width, dimensionsScope.height)) {
                                block.content.invoke(dimensionsScope)
                            }

                            if (index != blocks.lastIndex) divider(index, width, Dimens.DIVIDER_SIZE)
                        }
                    }
                }
            }
        }

    @Composable
    private fun divider(index: Int, width: Dp, height: Dp) = Box(
        Modifier.size(width, height).pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()

                when (blockListType) {
                    BlockListType.ROW -> {
                        val currSize = blocks[index].deltaWidthState.value + dragAmount.x.dp
                        val nextSize = blocks[index + 1].deltaWidthState.value - dragAmount.x.dp
                        blocks[index].deltaWidthState.component2().invoke(currSize)
                        blocks[index + 1].deltaWidthState.component2().invoke(nextSize)
                    }

                    BlockListType.COLUMN -> {
                        val currSize = blocks[index].deltaHeightState.value + dragAmount.y.dp
                        val nextSize = blocks[index + 1].deltaHeightState.value - dragAmount.y.dp
                        blocks[index].deltaHeightState.component2().invoke(currSize)
                        blocks[index + 1].deltaHeightState.component2().invoke(nextSize)
                    }
                }
            }
        })

    companion object {
        fun calculateDimension(
            maxDimension: Dp, block: WindowBlock, blocks: List<WindowBlock>
        ) = maxDimension * block.weight / blocks.sumOf { it.weight.toDouble() }.toFloat()
    }
}