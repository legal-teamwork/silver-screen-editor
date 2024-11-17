package org.legalteamwork.silverscreen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.rememberComponentRectPositionProvider
import org.legalteamwork.silverscreen.shortcut.Shortcut
import org.legalteamwork.silverscreen.shortcut.ShortcutManager

/**
 * Создание вкладки в системном меню
 *
 * @param[text] Текст вкладки (File, Edit, etc)
 * @param[mnemonic] Добавляет шорткат 'Alt+mnemonic', открывающий меню по данной вкладке
 * @param[enabled] Включена ли данная вкладка
 * @param[content] Контент меню
 */
@Composable
fun CustomMenu(
    text: String,
    mnemonic: Key? = null,
    enabled: Boolean = true,
    content: @Composable MenuScope.() -> Unit
) {
    var isActive by remember { mutableStateOf(false) }

    if (mnemonic != null) {
        val shortcut = Shortcut(
            key = mnemonic,
            alt = true
        )

        remember {
            ShortcutManager.addShortcut(shortcut) {
                isActive = !isActive
                true
            }
        }
    }

    Box(
        modifier = Modifier
            .background(if (isActive) selectedMenuBackground else menuBackground)
            .clickable(enabled = enabled) {
                logger.debug { "Activating $text" }
                isActive = true
            }
    ) {
        Text(
            text = text,
            color = if (isActive) menuActiveTextColor else menuTextColor,
            modifier = Modifier.padding(5.dp)
        )

        if (isActive) {
            val popupPositionProvider = rememberComponentRectPositionProvider(
                anchor = Alignment.BottomStart,
                alignment = Alignment.BottomEnd,
                offset = DpOffset(5.dp, 5.dp),
            )

            Popup(
                popupPositionProvider = popupPositionProvider,
                onDismissRequest = { isActive = false },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    clippingEnabled = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .wrapContentHeight()
                        .background(menuItemBackground, RoundedCornerShape(5.dp))
                        .border(1.dp, menuItemBorder, RoundedCornerShape(5.dp))
                        .shadow(5.dp, RoundedCornerShape(5.dp))
                ) {
                    Column {
                        val scope = object : MenuScope {
                            override fun onMenuClose() {
                                isActive = false
                            }

                        }

                        scope.content()
                    }
                }
            }
        }
    }
}
