package org.legalteamwork.silverscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition
import mu.KotlinLogging
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.SaveManager
import org.legalteamwork.silverscreen.rm.openFileDialog

val menuBarBackground = Color(0xFF000000)
val menuBackground = Color(0xFF000000)
val selectedMenuBackground = Color(0xFF444444)

val menuTextColor = Color(0xFFFFFFFF)
val menuActiveTextColor = Color(0xFFFFFFFF)

val menuItemBackground = Color(0xFF222222)
val menuItemBorder = Color(0xFFAAAAAA)
val menuAcceleratorColor = Color(0xFFAAAAAA)

val logger = KotlinLogging.logger {}

@Composable
fun MenuBarCompose() {
    CustomMenuBar {
        CustomMenu("File", mnemonic = 'F') {
            CustomItem(text = "New", shortcut = CustomKeyShortcut("N", ctrl = true)) {
                logger.debug { "MenuBar Action: New" }
                // TODO
            }
            CustomItem(text = "Open", shortcut = CustomKeyShortcut("O", ctrl = true)) {
                logger.debug { "MenuBar Action: Open" }

                // FIXME:
                val filenameSet = openFileDialog(null, "Open project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.load(filenameSet.first().path)
                }
            }

            Divider(color = menuItemBorder, thickness = 1.dp)

            CustomItem(
                text = "Import",
                shortcut = CustomKeyShortcut("I", ctrl = true)
            ) {
                logger.debug { "MenuBar Action: Import" }
                ResourceManager.addSourceTriggerActivity()
            }
            CustomItem(
                text = "Export",
                shortcut = CustomKeyShortcut("R", ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Export" }
                // TODO
            }

            Divider(color = menuItemBorder, thickness = 1.dp)

            CustomItem(text = "Save", shortcut = CustomKeyShortcut("S", ctrl = true)) {
                logger.debug { "MenuBar Action: Save" }

                // FIXME:
                val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            }
            CustomItem(
                text = "Save as",
                shortcut = CustomKeyShortcut("S", ctrl = true, shift = true)
            ) {
                logger.debug { "MenuBar Action: Save as" }

                // FIXME:
                val filenameSet = openFileDialog(null, "Save project", listOf("json"), false)

                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            }
            CustomItem(
                text = "Enable/Disable auto save",
                shortcut = CustomKeyShortcut("E", ctrl = true, shift = true)
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
        modifier = Modifier.fillMaxWidth().background(menuBarBackground)
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
    content: @Composable ColumnScope.() -> Unit
) {
    var isActive by remember { mutableStateOf(false) }

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
                        .width(300.dp)
                        .wrapContentHeight()
                        .background(menuItemBackground, RoundedCornerShape(5.dp))
                        .border(1.dp, menuItemBorder, RoundedCornerShape(5.dp))
                        .shadow(5.dp, RoundedCornerShape(5.dp))
                ) {
                    Column(content = content)
                }
            }
        }
    }
}

@Composable
fun CustomItem(
    text: String,
    enabled: Boolean = true,
    shortcut: CustomKeyShortcut? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
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
