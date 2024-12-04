package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.Composable

/**
 * Базовый интерфейс для окон приложения
 */
interface WindowBlock {
    /**
     * Компоуз контента окна
     */
    val content: @Composable DimensionsScope.() -> Unit
}