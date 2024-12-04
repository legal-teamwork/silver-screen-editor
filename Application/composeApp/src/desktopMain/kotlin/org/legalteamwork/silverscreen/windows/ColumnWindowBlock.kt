package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
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

class ColumnWindowBlock(
    private val blocksWithHeights: List<BlockWithWeight>,
) : ListWindowBlock(blocksWithHeights) {
    override fun calculateInitialWidth(width: Dp, weight: Float): Dp = width

    override fun calculateInitialHeight(height: Dp, weight: Float): Dp {
        val maxSize = height - Dimens.DIVIDER_SIZE * (blocksWithHeights.size - 1)
        val weightRatio = weight / blocksWithHeights.sumOf { it.weight.toDouble() }.toFloat()

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

                        var increaseDelta by heightDelta[index]
                        increaseDelta += dragAmount.y.dp

                        val lowerIndices = (index + 1)..heightDelta.lastIndex
                        for (i in lowerIndices) {
                            var lowerDelta by heightDelta[i]
                            lowerDelta -= dragAmount.y.dp / lowerIndices.count()
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

    override fun adaptDeltas(width: Dp, height: Dp, widths: List<Dp>, heights: List<Dp>) {}
}