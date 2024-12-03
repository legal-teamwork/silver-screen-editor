package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable

fun terminal(
    initialSize: Float,
    content: @Composable DimensionsScope.() -> Unit
) = TerminalBlock(initialSize, content)

fun row(
    initialSize: Float,
    vararg blocks: WindowBlock
) = RowWindowBlock(initialSize, blocks.asList())

fun column(
    initialSize: Float,
    vararg blocks: WindowBlock
) = ColumnWindowBlock(initialSize, blocks.asList())
