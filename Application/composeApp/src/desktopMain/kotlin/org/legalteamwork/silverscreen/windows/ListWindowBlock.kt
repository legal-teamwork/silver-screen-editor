package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

abstract class ListWindowBlock(
    private val blocksWithWeights: List<BlockWithWeight>,
) : WindowBlock {
    protected val dividerXOffsets: List<MutableState<Dp>> =
        List(blocksWithWeights.size + 1) { mutableStateOf(0.dp) }
    protected val dividerYOffsets: List<MutableState<Dp>> =
        List(blocksWithWeights.size + 1) { mutableStateOf(0.dp) }

    abstract fun calculateInitialWidth(width: Dp, weight: Float): Dp
    abstract fun calculateInitialHeight(height: Dp, weight: Float): Dp

    abstract val divider: @Composable (index: Int, width: Dp, height: Dp) -> Unit
    abstract val listComposable: @Composable DimensionsScope.(content: @Composable DimensionsScope.() -> Unit) -> Unit
    abstract fun adaptOffsets(width: Dp, height: Dp, widths: List<Dp>, heights: List<Dp>)

    override val content: @Composable DimensionsScope.() -> Unit = {
        val widths = blocksWithWeights.map { calculateInitialWidth(width, it.weight) }
        val heights = blocksWithWeights.map { calculateInitialHeight(height, it.weight) }
        adaptOffsets(width, height, widths, heights)

        listComposable {
            for (index in blocksWithWeights.indices) {
                val block = blocksWithWeights[index].block
                val blockWidth = widths[index] - dividerXOffsets[index].value + dividerXOffsets[index + 1].value
                val blockHeight = heights[index] - dividerYOffsets[index].value + dividerYOffsets[index + 1].value
                val dimensionsScope = DimensionsScope(blockWidth, blockHeight)

                Box(Modifier.size(dimensionsScope.width, dimensionsScope.height)) {
                    block.content.invoke(dimensionsScope)
                }

                if (index != blocksWithWeights.lastIndex) divider(index, width, height)
            }
        }
    }
}
