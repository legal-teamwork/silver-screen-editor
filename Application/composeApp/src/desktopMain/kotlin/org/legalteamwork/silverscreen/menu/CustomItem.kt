package org.legalteamwork.silverscreen.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.shortcut.Shortcut
import org.legalteamwork.silverscreen.shortcut.ShortcutManager

/**
 * Создание айтема в системном меню
 *
 * @param[text] Текст айтема
 * @param[enabled] Включен ли он
 * @param[shortcut] Шорткат, который триггерит активность айтема
 * @param[onClick] Активность айтема
 */
@Composable
fun MenuScope.CustomItem(
    text: String,
    enabled: Boolean = true,
    shortcut: Shortcut? = null,
    onClick: () -> Unit
) {
    if (shortcut != null) {
        remember {
            ShortcutManager.addShortcut(shortcut) {
                onClick()
                true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                enabled = enabled,
            ) {
                onClick()
                onMenuClose()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = menuTextColor,
                modifier = Modifier.padding(5.dp)
            )

            if (shortcut != null) {
                Text(
                    text = shortcut.toString(),
                    color = menuAcceleratorColor,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}