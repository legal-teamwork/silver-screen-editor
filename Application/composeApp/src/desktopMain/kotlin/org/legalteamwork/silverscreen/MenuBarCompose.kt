package org.legalteamwork.silverscreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import mu.KotlinLogging
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.SaveManager
import org.legalteamwork.silverscreen.rm.openFileDialog
import javax.swing.UIManager

val menuBackground = Color(0xFF000000)
val selectedMenuBackground = Color(0xFF666666)

val menuTextColor = Color(0xFFFFFFFF)
val menuActiveTextColor = Color(0xFFFFFFFF)

val menuItemBackground = Color(0xFF222222)
val menuItemBorder = Color(0xFFAAAAAA)
val menuAcceleratorColor = Color(0xFFAAAAAA)
val menuItemSelectedBackground = Color(0xFF444444)
val logger = KotlinLogging.logger {}

@Composable
fun FrameWindowScope.MenuBarCompose() {
    // Color definitions:
    // Edit menu bar colors:
//    UIManager.put("MenuBar.background", appBackground)
//    UIManager.put("MenuBar.border", appBackground)
//    UIManager.put("Menu.background", menuBackground)
//    UIManager.put("Menu.foreground", menuTextColor)
//    UIManager.put("Menu.selectionBackground", selectedMenuBackground)
//    UIManager.put("Menu.selectionForeground", menuTextColor)
//    UIManager.put("Menu.borderPainted", false)
//    UIManager.put("MenuItem.background", menuItemBackground)
//    UIManager.put("MenuItem.borderPainted", false)
//    UIManager.put("MenuItem.foreground", menuTextColor)
//    UIManager.put("MenuItem.acceleratorForeground", menuAcceleratorColor)
//    UIManager.put("MenuItem.acceleratorSelectionForeground", menuAcceleratorColor)
//    UIManager.put("MenuItem.selectionBackground", menuItemSelectedBackground)
//    UIManager.put("MenuItem.selectionForeground", menuTextColor)
//    UIManager.put("PopupMenu.border", popupWindowBorderColor)

    // Menu bar compose:
    CustomMenuBar {
        CustomMenu("File", mnemonic = 'F') {
            CustomItem(text = "New", shortcut = KeyShortcut(Key.N, ctrl = true)) {
                logger.debug { "MenuBar Action: New" }
                // TODO
            }
            CustomItem(text = "Open", shortcut = KeyShortcut(Key.O, ctrl = true)) {
                logger.debug { "MenuBar Action: Open" }
                val filenameSet = openFileDialog(null, "Open project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.load(filenameSet.first().path)
                }
            }
            CustomItem(
                text = "Import",
                shortcut = KeyShortcut(Key.I, ctrl = true)
            ) {
                logger.debug { "MenuBar Action: Import" }
                ResourceManager.addSourceTriggerActivity()
            }
            CustomItem(
                text = "Export",
                shortcut = KeyShortcut(Key.R, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Export" }
                // TODO
            }
            CustomItem(text = "Save", shortcut = KeyShortcut(Key.S, ctrl = true)) {
                logger.debug { "MenuBar Action: Save" }
                val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            }
            CustomItem(
                text = "Save as",
                shortcut = KeyShortcut(Key.S, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Save as" }
                val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            }
            CustomItem(
                text = "Enable/Disable auto save",
                shortcut = KeyShortcut(Key.E, ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Enable/Disable auto save" }
                // TODO
            }
        }
    }
}

@Composable
fun CustomMenuBar(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().background(Color.Red)
    ) {
        Row {
            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomMenu(
    text: String,
    mnemonic: Char? = null,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    var isActive by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .background(if (isActive) selectedMenuBackground else menuBackground)
            .onClick {
                logger.debug { "Activating $text" }
                isActive = true
            }
    ) {
        Text(
            text = text,
            color = if (isActive) menuActiveTextColor else menuTextColor,
            modifier = Modifier.padding(3.dp)
        )

        if (isActive) {
            Popup(
                popupPositionProvider = rememberPopupPositionProviderAtPosition(
                    positionPx = Offset.Zero,
                    offset = DpOffset(50.dp, 50.dp),
                ),
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
                        .width(250.dp)
                        .wrapContentHeight()
                        .background(menuItemBackground)
                        .border(1.dp, menuItemBorder)
                ) {
                    Column {
                        content()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomItem(
    text: String,
    icon: Painter? = null,
    enabled: Boolean = true,
    mnemonic: Char? = null,
    shortcut: KeyShortcut? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text, color = menuTextColor, modifier = Modifier.padding(3.dp))
            Text("Ctrl+N", color = menuTextColor, modifier = Modifier.padding(3.dp))
        }
    }
}
