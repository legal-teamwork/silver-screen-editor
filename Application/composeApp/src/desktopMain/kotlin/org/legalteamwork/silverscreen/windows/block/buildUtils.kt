package org.legalteamwork.silverscreen.windows.block

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import org.legalteamwork.silverscreen.windows.data.BlockWithWeight
import org.legalteamwork.silverscreen.windows.data.DimensionsScope

fun terminal(
    minWidth: Dp,
    minHeight: Dp,
    maxWidth: Dp,
    maxHeight: Dp,
    content: @Composable DimensionsScope.() -> Unit
): WindowBlock = TerminalBlock(minWidth, minHeight, maxWidth, maxHeight, content)

fun row(
    vararg blocks: BlockWithWeight
): WindowBlock = RowWindowBlock(blocks.asList())

fun column(
    vararg blocks: BlockWithWeight
): WindowBlock = ColumnWindowBlock(blocks.asList())

infix fun Float.with(block: WindowBlock): BlockWithWeight = BlockWithWeight(this, block)
