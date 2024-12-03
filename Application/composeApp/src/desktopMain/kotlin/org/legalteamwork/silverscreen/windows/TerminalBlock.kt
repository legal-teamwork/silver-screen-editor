package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable

class TerminalBlock(
    override val weight: Float,
    override val content: @Composable DimensionsScope.() -> Unit
) : AbstractWindowBlock()
