package org.legalteamwork.silverscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import javax.swing.UIManager

@Composable
fun FrameWindowScope.MenuBarCompose() {
    // Color definitions:
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
            Item(text = "New", shortcut = KeyShortcut(Key.N, ctrl = true)) { println("[Triggered] New") }
            Item(text = "Open", shortcut = KeyShortcut(Key.O, ctrl = true)) { println("[Triggered] Open") }
            Item(text = "Import", shortcut = KeyShortcut(Key.I, ctrl = true)) { println("[Triggered] Import") }
            Item(
                text = "Export",
                shortcut = KeyShortcut(Key.R, ctrl = true, shift = true)
            ) { println("[Triggered] Export") }
            Item(text = "Save", shortcut = KeyShortcut(Key.S, ctrl = true)) { println("[Triggered] Save") }
            Item(
                text = "Save as",
                shortcut = KeyShortcut(Key.S, ctrl = true, shift = true)
            ) { println("[Triggered] Save as") }
            Item(
                text = "Enable/Disable auto save",
                shortcut = KeyShortcut(Key.E, ctrl = true, shift = true)
            ) { println("[Triggered] Enable/Disable auto save") }
        }
    }
}