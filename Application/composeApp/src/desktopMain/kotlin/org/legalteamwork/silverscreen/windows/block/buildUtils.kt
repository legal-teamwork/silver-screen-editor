package org.legalteamwork.silverscreen.windows.block

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import org.legalteamwork.silverscreen.windows.data.BlockWithWeight
import org.legalteamwork.silverscreen.windows.data.DimensionsScope

/**
 * Короткий метод создания [TerminalBlock]
 */
fun terminal(
    minWidth: Dp,
    minHeight: Dp,
    maxWidth: Dp,
    maxHeight: Dp,
    content: @Composable DimensionsScope.() -> Unit
): WindowBlock = TerminalBlock(minWidth, minHeight, maxWidth, maxHeight, content)

/**
 * Короткий метод создания [RowWindowBlock]
 */
fun row(
    vararg blocks: BlockWithWeight
): WindowBlock = RowWindowBlock(blocks.asList())

/**
 * Короткий метод создания [ColumnWindowBlock]
 */
fun column(
    vararg blocks: BlockWithWeight
): WindowBlock = ColumnWindowBlock(blocks.asList())

/**
 * Короткий метод создания [BlockWithWeight]
 */
infix fun Float.with(block: WindowBlock): BlockWithWeight = BlockWithWeight(this, block)
