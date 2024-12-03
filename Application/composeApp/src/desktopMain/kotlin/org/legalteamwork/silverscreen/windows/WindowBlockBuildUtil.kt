package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable

fun terminal(
    content: @Composable DimensionsScope.() -> Unit
): WindowBlock = TerminalBlock(content)

fun row(
    vararg blocks: BlockWithWeight
): WindowBlock = RowWindowBlock(blocks.asList())

fun column(
    vararg blocks: BlockWithWeight
): WindowBlock = ColumnWindowBlock(blocks.asList())

infix fun Float.with(block: WindowBlock): BlockWithWeight = BlockWithWeight(this, block)
