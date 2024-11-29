package org.legalteamwork.silverscreen.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.AppScope

val menuBarBackground = Color(0xFF000000)
val menuBackground = Color(0xFF000000)
val selectedMenuBackground = Color(0xFF444444)

val menuTextColor = Color(0xFFFFFFFF)
val menuActiveTextColor = Color(0xFFFFFFFF)

val menuItemBackground = Color(0xFF222222)
val menuItemBorder = Color(0xFFAAAAAA)
val menuAcceleratorColor = Color(0xFFAAAAAA)

val logger = KotlinLogging.logger {}

/**
 * Компоуз, ответственный за добавление системного меню
 */
@Composable
fun AppScope.MenuBarCompose() {
    var activeMenu by remember { menuBarData.activeMenu }

    MenuBar {
        for (menuData in menuBarData.menuList) {
            Menu(
                menuData.text,
                menuData.mnemonic,
                menuData.enabled,
                menuData == activeMenu,
                { activeMenu = menuData },
                { activeMenu = null },
            ) {
                for (menuItemData in menuData.menuItemList) {
                    MenuItem(
                        menuItemData.text.invoke(),
                        menuItemData.enabled,
                        menuItemData.shortcut,
                        menuItemData.onClick
                    )
                }
            }
        }
    }
}
