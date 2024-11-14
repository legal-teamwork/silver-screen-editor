package org.legalteamwork.silverscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import mu.KotlinLogging
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.SaveManager
import org.legalteamwork.silverscreen.rm.openFileDialog
import javax.swing.UIManager

@Composable
fun FrameWindowScope.MenuBarCompose() {
    // Color definitions:
    val logger = KotlinLogging.logger {}
    val appBackground = java.awt.Color(0x000000)
    val menuBackground = java.awt.Color(0x000000)
    val selectedMenuBackground = java.awt.Color(0x666666)
    val menuTextColor = java.awt.Color(0xFFFFFF)
    val menuAcceleratorColor = java.awt.Color(0xAAAAAA)
    val menuItemBackground = java.awt.Color(0x222222)
    val menuItemSelectedBackground = java.awt.Color(0x444444)
    val popupWindowBorderColor = java.awt.Color(0, 0, 0, 0)

    // Edit menu bar colors:
    UIManager.put("MenuBar.background", appBackground)
    UIManager.put("MenuBar.border", appBackground)
    UIManager.put("Menu.background", menuBackground)
    UIManager.put("Menu.foreground", menuTextColor)
    UIManager.put("Menu.selectionBackground", selectedMenuBackground)
    UIManager.put("Menu.selectionForeground", menuTextColor)
    UIManager.put("Menu.borderPainted", false)
    UIManager.put("MenuItem.background", menuItemBackground)
    UIManager.put("MenuItem.borderPainted", false)
    UIManager.put("MenuItem.foreground", menuTextColor)
    UIManager.put("MenuItem.acceleratorForeground", menuAcceleratorColor)
    UIManager.put("MenuItem.acceleratorSelectionForeground", menuAcceleratorColor)
    UIManager.put("MenuItem.selectionBackground", menuItemSelectedBackground)
    UIManager.put("MenuItem.selectionForeground", menuTextColor)
    UIManager.put("PopupMenu.border", popupWindowBorderColor)

    // Menu bar compose:
    MenuBar {
        Menu("File", mnemonic = 'F') {
            Item(text = "New", shortcut = KeyShortcut(Key.N, ctrl = true)) {
                logger.debug { "MenuBar Action: New" }
                // TODO
            }
            Item(text = "Open", shortcut = KeyShortcut(Key.O, ctrl = true)) {
                logger.debug { "MenuBar Action: Open" }
                val filenameSet = openFileDialog(null, "Open project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.load(filenameSet.first().path)
                }
            }
            Item(
                text = "Import",
                shortcut = KeyShortcut(Key.I, ctrl = true)
            ) {
                logger.debug { "MenuBar Action: Import" }
                ResourceManager.addSourceTriggerActivity()
            }
            Item(
                text = "Export",
                shortcut = KeyShortcut(Key.R, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Export" }
                // TODO
            }
            Item(text = "Save", shortcut = KeyShortcut(Key.S, ctrl = true)) {
                logger.debug { "MenuBar Action: Save" }
                val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            }
            Item(
                text = "Save as",
                shortcut = KeyShortcut(Key.S, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Save as" }
                val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            }
            Item(
                text = "Enable/Disable auto save",
                shortcut = KeyShortcut(Key.E, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Enable/Disable auto save" }
                // TODO
            }
        }
    }
}
