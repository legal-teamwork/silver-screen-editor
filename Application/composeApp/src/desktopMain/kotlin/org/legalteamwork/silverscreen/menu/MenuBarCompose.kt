package org.legalteamwork.silverscreen.menu

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.menu.*
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.save.EditorSettings
import org.legalteamwork.silverscreen.shortcut.Shortcut

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
    MenuBar {
        Menu(Strings.FILE_MENU_TAG, mnemonic = Key.F) {
            MenuItem(text = Strings.FILE_NEW_ITEM, shortcut = Shortcut(Key.N, ctrl = true)) {
                commandManager.execute(NewCommand())
            }
            MenuItem(text = Strings.FILE_OPEN_ITEM, shortcut = Shortcut(Key.O, ctrl = true)) {
                commandManager.execute(OpenCommand())
            }

            Divider(color = menuItemBorder, thickness = 1.dp)

            MenuItem(
                text = Strings.FILE_IMPORT_ITEM, shortcut = Shortcut(Key.I, ctrl = true)
            ) {
                commandManager.execute(ImportCommand(resourceManager))
            }

            MenuItem(
                text = Strings.FILE_EXPORT_ITEM, shortcut = Shortcut(Key.R, ctrl = true, shift = true)
            ) {
                commandManager.execute(ExportCommand())
            }

            Divider(color = menuItemBorder, thickness = 1.dp)

            MenuItem(text = Strings.FILE_SAVE_ITEM, shortcut = Shortcut(Key.S, ctrl = true)) {
                commandManager.execute(SaveCommand())
            }
            MenuItem(
                text = Strings.FILE_SAVE_AS_ITEM, shortcut = Shortcut(Key.S, ctrl = true, shift = true)
            ) {
                commandManager.execute(SaveAsCommand())
            }

            Divider(color = menuItemBorder, thickness = 1.dp)

            MenuItem(
                text = Strings.PROJECT_SETTINGS_ITEM, shortcut = Shortcut(Key.P, ctrl = true)
            ) {
                commandManager.execute(ProjectSettingsOpenCommand())
            }

            MenuItem(
                text = if (EditorSettings.get().autosaveEnabled.value)
                    Strings.FILE_AUTO_SAVE_ITEM_ON
                else
                    Strings.FILE_AUTO_SAVE_ITEM_OFF,
                shortcut = Shortcut(Key.E, ctrl = true, shift = true)
            ) {
                commandManager.execute(AutosaveEnableOrDisableCommand())
            }
        }
    }
}
