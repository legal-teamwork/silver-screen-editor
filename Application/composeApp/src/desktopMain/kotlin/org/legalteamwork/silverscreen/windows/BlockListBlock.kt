package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.resources.Dimens

class BlockListBlock(
    override val initialSize: Float,
    val initialSizeType: InitialSizeType,
    val blocks: List<WindowBlock>,
    val blockListType: BlockListType
) : AbstractWindowBlock() {
    override fun configure(
        deltaWidthListAdd: (MutableState<Dp>) -> Unit, deltaHeightListAdd: (MutableState<Dp>) -> Unit
    ) {
        super.configure(deltaWidthListAdd, deltaHeightListAdd)

        for (block in blocks) {
            block.configure(deltaWidthListAdd, deltaHeightListAdd)
        }
    }

    override val content: @Composable DimensionsScope.() -> Unit
        get() = {
            Box(Modifier.fillMaxSize()) {
                when (blockListType) {
                    BlockListType.ROW -> Row {
                        for ((index, block) in blocks.withIndex()) {
                            val maxWidth = width - Dimens.DIVIDER_SIZE * (blocks.size - 1)
                            val blockWidth = calculateDimension(
                                initialSizeType,
                                maxWidth,
                                block,
                                blocks
                            )
                            val blockHeight = height
                            val dimensionsScope = DimensionsScope(
                                blockWidth + block.deltaWidthState.value,
                                blockHeight + block.deltaHeightState.value
                            )

                            Box(
                                Modifier.size(
                                    dimensionsScope.width,
                                    dimensionsScope.height
                                )
                            ) {
                                block.content.invoke(dimensionsScope)
                            }

                            if (index != blocks.lastIndex) divider(index, Dimens.DIVIDER_SIZE, height)
                        }
                    }

                    BlockListType.COLUMN -> Column {
                        for ((index, block) in blocks.withIndex()) {
                            val maxHeight = height - Dimens.DIVIDER_SIZE * (blocks.size - 1)
                            val blockWidth = width
                            val blockHeight = calculateDimension(
                                initialSizeType,
                                maxHeight,
                                block,
                                blocks
                            )

                            val dimensionsScope = DimensionsScope(
                                blockWidth + block.deltaWidthState.value,
                                blockHeight + block.deltaHeightState.value
                            )

                            Box(
                                Modifier.size(
                                    dimensionsScope.width,
                                    dimensionsScope.height
                                )
                            ) {
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
        Modifier
            .size(width, height)
            .pointerInput(Unit) {
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
            }
    )

    companion object {
        fun calculateDimension(
            initialSizeType: InitialSizeType,
            maxDimension: Dp,
            block: WindowBlock,
            blocks: List<WindowBlock>
        ) = when (initialSizeType) {
            InitialSizeType.DP -> {
                block.initialSize.dp
            }

            InitialSizeType.PARTIAL -> {
                maxDimension * block.initialSize / blocks.sumOf { it.initialSize.toDouble() }.toFloat()
            }
        }
    }
}