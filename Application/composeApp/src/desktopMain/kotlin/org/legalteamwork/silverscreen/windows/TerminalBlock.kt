package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

class TerminalBlock(
    override val minWidth: Dp,
    override val minHeight: Dp,
    override val maxWidth: Dp,
    override val maxHeight: Dp,
    override val content: @Composable DimensionsScope.() -> Unit,
) : WindowBlock
