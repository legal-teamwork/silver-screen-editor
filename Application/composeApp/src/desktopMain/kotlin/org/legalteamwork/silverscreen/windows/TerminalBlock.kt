package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable

class TerminalBlock(
    override val initialSize: Float,
    override val content: @Composable DimensionsScope.() -> Unit
) : AbstractWindowBlock()
