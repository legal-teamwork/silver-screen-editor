package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Абстрактный класс для отображения ряда окошек в одной полосе, разделенные разделителем,
 * позволяющим изменять размеры окон.
 * Размеры под-окон определяются так: у каждого окна высчитываются инициирующиеся размеры,
 * исходя из размеров, выделенных для ряда, и, чтобы можно было менять размеры, добавляется сдвиг, дельта
 * для каждого размера под-окна.
 */
abstract class ListWindowBlock(
    private val blocksWithWeights: List<BlockWithWeight>,
) : WindowBlock {
    /**
     * Дельта ширины каждого под-окна.
     * Инвариант: должны в сумме всегда давать 0 dp
     */
    protected val widthDelta: List<MutableState<Dp>> =
        List(blocksWithWeights.size) { mutableStateOf(0.dp) }

    /**
     * Дельта высоты каждого под-окна.
     * Инвариант: должны в сумме всегда давать 0 dp
     */
    protected val heightDelta: List<MutableState<Dp>> =
        List(blocksWithWeights.size) { mutableStateOf(0.dp) }

    /**
     * Метод, высчитывающий инициирующую ширину под-окна с весом [weight] при выделенное ширине на ряд [width]
     *
     * @param[width] выделенная ширина под весь ряд
     * @param[weight] вес под-окна
     *
     * @return [Dp] ширина под-окна
     */
    abstract fun calculateInitialWidth(width: Dp, weight: Float): Dp

    /**
     * Метод, высчитывающий инициирующую высоту под-окна с весом [weight] при выделенной высоте на ряд [height]
     *
     * @param[height] выделенная высота под весь ряд
     * @param[weight] вес под-окна
     *
     * @return [Dp] высота под-окна
     */
    abstract fun calculateInitialHeight(height: Dp, weight: Float): Dp

    /**
     * Адаптирует сдвиги размеров [widthDelta] и [heightDelta] так, чтобы сохранялся их инвариант
     * и новые размеры были допустимые (входили в определенные ограничения)
     *
     * @param[width] выделенная ширина под весь ряд
     * @param[height] выделенная высота под весь ряд
     * @param[widths] массив ширин инициации для всех под-окон
     * @param[heights] массив высот инициации для всех под-окон
     */
    abstract fun adaptDeltas(width: Dp, height: Dp, widths: List<Dp>, heights: List<Dp>)

    /**
     * Компоуз разделителя
     */
    abstract val divider: @Composable (index: Int, width: Dp, height: Dp) -> Unit

    /**
     * Компоуз обёртки ряда (например, Row или Column)
     */
    abstract val listComposable: @Composable DimensionsScope.(content: @Composable DimensionsScope.() -> Unit) -> Unit

    override val content: @Composable DimensionsScope.() -> Unit = {
        val widths = blocksWithWeights.map { calculateInitialWidth(width, it.weight) }
        val heights = blocksWithWeights.map { calculateInitialHeight(height, it.weight) }
        adaptDeltas(width, height, widths, heights)

        val blockWidths = widths.zip(widthDelta).map { it.first + it.second.value }
        val blockHeights = heights.zip(heightDelta).map { it.first + it.second.value }

        listComposable {
            for (index in blocksWithWeights.indices) {
                val block = blocksWithWeights[index].block
                val blockWidth = blockWidths[index]
                val blockHeight = blockHeights[index]
                val dimensionsScope = DimensionsScope(blockWidth, blockHeight)

                Box(Modifier.size(dimensionsScope.width, dimensionsScope.height)) {
                    block.content.invoke(dimensionsScope)
                }

                if (index != blocksWithWeights.lastIndex) divider(index, width, height)
            }
        }
    }
}
