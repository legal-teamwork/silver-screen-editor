package org.legalteamwork.silverscreen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Создание системного меню бара
 *
 * @param[content] Контент бара
 */
@Composable
fun CustomMenuBar(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().background(menuBarBackground)
    ) {
        Row {
            content()
        }
    }
}
