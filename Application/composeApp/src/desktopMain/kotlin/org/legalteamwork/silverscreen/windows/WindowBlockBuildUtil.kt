package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable

fun terminal(
    initialSize: Float,
    content: @Composable DimensionsScope.() -> Unit
) = TerminalBlock(initialSize, content)

fun row(
    initialSize: Float,
    initialSizeType: InitialSizeType,
    vararg blocks: WindowBlock
) = BlockListBlock(initialSize, initialSizeType, blocks.asList(), BlockListType.ROW)

fun column(
    initialSize: Float,
    initialSizeType: InitialSizeType,
    vararg blocks: WindowBlock
) = BlockListBlock(initialSize, initialSizeType, blocks.asList(), BlockListType.COLUMN)
