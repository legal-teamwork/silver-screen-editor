package org.legalteamwork.silverscreen.windows.block

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.windows.data.BlockWithWeight
import org.legalteamwork.silverscreen.windows.data.DimensionsScope

/**
 * Класс для отображения вертикального ряда окошек в одной полосе, разделенные разделителем,
 * позволяющим изменять размеры окон только в вертикальной размерности (высота)
 */
class ColumnWindowBlock(
    blocksWithHeights: List<BlockWithWeight>,
) : ListWindowBlock(blocksWithHeights) {
    override val minWidth: Dp
        get() = dimensions.maxOf { it.minWidth }
    override val minHeight: Dp
        get() = dimensions.fold(0.dp) { acc, item -> acc + item.minHeight }
    override val maxWidth: Dp
        get() = dimensions.minOf { it.maxWidth }
    override val maxHeight: Dp
        get() = dimensions.fold(0.dp) { acc, item -> acc + item.maxHeight }

    override fun calculateInitialWidth(width: Dp, weight: Float): Dp = width

    override fun calculateInitialHeight(height: Dp, weight: Float): Dp {
        val maxSize = height - Dimens.DIVIDER_SIZE * (dimensions.size - 1)
        val weightRatio = weight / dimensions.sumOf { it.weight.toDouble() }.toFloat()

        return maxSize * weightRatio
    }

    override val divider: @Composable (index: Int, width: Dp, height: Dp) -> Unit = { index, width, _ ->
        Box(
            Modifier
                .size(width, Dimens.DIVIDER_SIZE)
                .pointerHoverIcon(PointerIcon.Hand)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        val nextIndices = (index + 1)..dimensions.lastIndex
                        val possibleNextAddition = nextIndices
                            .map { dimensions[it] }
                            .map { it.getDeltaHeightPossibleRange() }
                            .fold(0.dp..0.dp) { acc, item ->
                                (acc.start + item.start)..(acc.endInclusive + item.endInclusive)
                            }
                        val possibleCurrAddition = possibleNextAddition.let { -it.endInclusive..-it.start }
                        val currAddition = dragAmount.y.dp.coerceIn(possibleCurrAddition)

                        val (added, _) = dimensions[index].addToDeltaHeight(currAddition)
                        var needToAccumulate = -added
                        var currentIndex = index + 1

                        while (currentIndex < dimensions.size) {
                            val (_, ignored) = dimensions[currentIndex].addToDeltaHeight(needToAccumulate)
                            needToAccumulate = ignored
                            currentIndex++
                        }
                    }
                })
    }

    override val listComposable: @Composable DimensionsScope.(content: @Composable DimensionsScope.() -> Unit) -> Unit = { content ->
        Box(Modifier.fillMaxSize()) {
            Column {
                content()
            }
        }
    }

}