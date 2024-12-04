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
    private val blocksWithHeights: List<BlockWithWeight>,
) : ListWindowBlock(blocksWithHeights) {
    override fun calculateInitialWidth(width: Dp, weight: Float): Dp {
        val maxSize = width - Dimens.DIVIDER_SIZE * (blocksWithHeights.size - 1)
        val weightRatio = weight / blocksWithHeights.sumOf { it.weight.toDouble() }.toFloat()

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

                        var increaseDelta by widthDelta[index]
                        increaseDelta += dragAmount.x.dp

                        val lowerIndices = (index + 1)..widthDelta.lastIndex
                        for (i in lowerIndices) {
                            var lowerDelta by widthDelta[i]
                            lowerDelta -= dragAmount.x.dp / lowerIndices.count()
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

    override fun adaptDeltas(width: Dp, height: Dp, widths: List<Dp>, heights: List<Dp>) {}
}