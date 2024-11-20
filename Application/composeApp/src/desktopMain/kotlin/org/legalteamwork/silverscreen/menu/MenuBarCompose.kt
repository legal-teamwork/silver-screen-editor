package org.legalteamwork.silverscreen.menu

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.SaveManager
import org.legalteamwork.silverscreen.rm.openFileDialog
import org.legalteamwork.silverscreen.shortcut.Shortcut
import org.legalteamwork.silverscreen.resources.Strings

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
fun MenuBarCompose() {
    MenuBar {
        Menu(Strings.FILE_MENU_TAG, mnemonic = Key.F) {
            MenuItem(text = Strings.FILE_NEW_ITEM, shortcut = Shortcut(Key.N, ctrl = true)) {
                logger.debug { "MenuBar Action: New" }
                // TODO
            }
            MenuItem(text = Strings.FILE_OPEN_ITEM, shortcut = Shortcut(Key.O, ctrl = true)) {
                logger.debug { "MenuBar Action: Open" }

                // FIXME:
                val filenameSet = openFileDialog(null, "Open project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.load(filenameSet.first().path)
                }
            }

            Divider(color = menuItemBorder, thickness = 1.dp)

            MenuItem(
                text = Strings.FILE_IMPORT_ITEM, shortcut = Shortcut(Key.I, ctrl = true)
            ) {
                logger.debug { "MenuBar Action: Import" }
                ResourceManager.addSourceTriggerActivity()
            }
            MenuItem(
                text = Strings.FILE_EXPORT_ITEM, shortcut = Shortcut(Key.R, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Export" }
                // TODO
            }

            Divider(color = menuItemBorder, thickness = 1.dp)

            MenuItem(text = Strings.FILE_SAVE_ITEM, shortcut = Shortcut(Key.S, ctrl = true)) {
                logger.debug { "MenuBar Action: Save" }

                // FIXME:
                val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            }
            MenuItem(
                text = Strings.FILE_SAVE_AS_ITEM, shortcut = Shortcut(Key.S, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Save as" }

                // FIXME:
                val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            }
            MenuItem(
                text = Strings.FILE_AUTO_SAVE_ITEM, shortcut = Shortcut(Key.E, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Enable/Disable auto save" }
                // TODO
            }
        }
    }
}
