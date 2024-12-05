package org.legalteamwork.silverscreen.windows.block

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import org.legalteamwork.silverscreen.windows.data.BlockWithDimensions
import org.legalteamwork.silverscreen.windows.data.BlockWithWeight
import org.legalteamwork.silverscreen.windows.data.DimensionsScope

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
    protected val dimensions: List<BlockWithDimensions> = blocksWithWeights.map { BlockWithDimensions(it.block, it.weight) }

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
     * Адаптирует сдвиги размеров в [dimensions] так, чтобы сохранялся их инвариант
     * и новые размеры были допустимые (входили в определенные ограничения)
     *
     * @param[width] выделенная ширина под весь ряд
     * @param[height] выделенная высота под весь ряд
     */
    abstract fun adaptDeltas(width: Dp, height: Dp)

    /**
     * Компоуз разделителя
     */
    abstract val divider: @Composable (index: Int, width: Dp, height: Dp) -> Unit

    /**
     * Компоуз обёртки ряда (например, Row или Column)
     */
    abstract val listComposable: @Composable DimensionsScope.(content: @Composable DimensionsScope.() -> Unit) -> Unit

    override val content: @Composable DimensionsScope.() -> Unit = {
        for (dimension in dimensions) {
            dimension.initiationWidth = calculateInitialWidth(width, dimension.weight)
            dimension.initiationHeight = calculateInitialHeight(height, dimension.weight)
        }

        adaptDeltas(width, height)

        listComposable {
            for ((index, dimension) in dimensions.withIndex()) {
                val block = blocksWithWeights[index].block
                val blockWidth = dimension.getWidth()
                val blockHeight = dimension.getHeight()
                val dimensionsScope = DimensionsScope(blockWidth, blockHeight)

                Box(Modifier.size(dimensionsScope.width, dimensionsScope.height)) {
                    block.content.invoke(dimensionsScope)
                }

                if (index != blocksWithWeights.lastIndex) divider(index, width, height)
            }
        }
    }
}
