package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun terminal(
    content: @Composable DimensionsScope.() -> Unit
): WindowBlock = TerminalBlock(0.dp, 0.dp, Dp.Infinity, Dp.Infinity, content)

fun terminal(
    minWidth: Dp,
    minHeight: Dp,
    maxWidth: Dp,
    maxHeight: Dp,
    content: @Composable DimensionsScope.() -> Unit
): WindowBlock = TerminalBlock(minWidth, minHeight, maxWidth, maxHeight, content)

fun row(
    vararg blocks: BlockWithWeight
): WindowBlock = RowWindowBlock(0.dp, 0.dp, Dp.Infinity, Dp.Infinity, blocks.asList())

fun column(
    vararg blocks: BlockWithWeight
): WindowBlock = ColumnWindowBlock(0.dp, 0.dp, Dp.Infinity, Dp.Infinity, blocks.asList())

infix fun Float.with(block: WindowBlock): BlockWithWeight = BlockWithWeight(this, block)
