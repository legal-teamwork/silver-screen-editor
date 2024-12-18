package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProviderAtPosition
import androidx.compose.ui.window.PopupProperties
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.resource.AddResourceCommand
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.NewFolderWindowTheme
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppScope.NewFolderWindow(
    contextWindowData: ContextWindowData,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    val position = contextWindowData.position
    val shape = RoundedCornerShape(5.dp)

    Popup(
        popupPositionProvider = PopupPositionProviderAtPosition(position, true, Offset.Zero, Alignment.BottomEnd, 4),
        onDismissRequest = onContextWindowClose,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .width(Dimens.WINDOW_WIDTH)
                .wrapContentHeight()
                .shadow(5.dp, shape = shape)
                .background(NewFolderWindowTheme.BACKGROUND_COLOR, shape = shape)
                .border(1.dp, NewFolderWindowTheme.BORDER_COLOR, shape = shape)
        ) {
            Column {
                var folderName by remember { mutableStateOf("New folder") }

                BasicTextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    textStyle = TextStyle(color = NewFolderWindowTheme.TEXT_COLOR),
                    cursorBrush = SolidColor(NewFolderWindowTheme.CURSOR_BRUSH_COLOR)
                ) { innerContent ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(10.dp)
                            .border(1.dp, color = NewFolderWindowTheme.INNER_BORDER_COLOR, shape = RoundedCornerShape(5.dp))
                            .background(color = NewFolderWindowTheme.INNER_BACKGROUND_COLOR, shape = RoundedCornerShape(5.dp))
                    ) {
                        Box(Modifier.padding(10.dp)) { innerContent() }
                    }
                }

                Box (modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    Button(
                        onClick = {
                            val folderResource = FolderResource(
                                mutableStateOf(folderName),
                                ResourceManager.currentFolder.value,
                                mutableStateListOf()
                            )

                            commandManager.execute(AddResourceCommand(resourceManager, folderResource))
                            onContextWindowClose()
                        },
                        modifier = Modifier.align(Alignment.Center).wrapContentSize()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}