package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.resources.Dimens

class RowWindowBlock(
    override val minWidth: Dp,
    override val minHeight: Dp,
    override val maxWidth: Dp,
    override val maxHeight: Dp,
    blocksWithHeights: List<BlockWithWeight>,
) : ListWindowBlock(blocksWithHeights) {
    override fun calculateInitialWidth(width: Dp, weight: Float): Dp {
        val maxSize = width - Dimens.DIVIDER_SIZE * (dimensions.size - 1)
        val weightRatio = weight / dimensions.sumOf { it.weight.toDouble() }.toFloat()

        return maxSize * weightRatio
    }

    override fun calculateInitialHeight(height: Dp, weight: Float): Dp = height

    override val divider: @Composable (index: Int, width: Dp, height: Dp) -> Unit = { index, _, height ->
        Box(
            Modifier
                .size(Dimens.DIVIDER_SIZE, height)
                .pointerHoverIcon(PointerIcon.Hand)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        val (added, _) = dimensions[index].addToDeltaWidth(dragAmount.x.dp)
                        var needToAccumulate = -added
                        var currentIndex = index + 1

                        while (needToAccumulate != 0.dp && currentIndex < dimensions.size) {
                            val (a, ignored) = dimensions[currentIndex].addToDeltaWidth(needToAccumulate)
                            needToAccumulate = ignored
                            currentIndex++
                        }
                    }
                })
    }

    override val listComposable: @Composable DimensionsScope.(content: @Composable DimensionsScope.() -> Unit) -> Unit = { content ->
        Box(Modifier.fillMaxSize()) {
            Row {
                content()
            }
        }
    }

    override fun adaptDeltas(width: Dp, height: Dp) {}
}