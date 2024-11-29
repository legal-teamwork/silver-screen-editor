package org.legalteamwork.silverscreen.menu

import androidx.compose.runtime.Composable
import org.legalteamwork.silverscreen.shortcut.Shortcut

data class MenuItemData(
    val text: @Composable () -> String,
    val enabled: Boolean = true,
    val shortcut: Shortcut? = null,
    val onClick: () -> Unit
)
