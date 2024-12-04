package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/**
 * Базовый интерфейс для окон приложения
 */
interface WindowBlock {
    val minWidth: Dp
    val minHeight: Dp
    val maxWidth: Dp
    val maxHeight: Dp

    /**
     * Компоуз контента окна
     */
    val content: @Composable DimensionsScope.() -> Unit
}