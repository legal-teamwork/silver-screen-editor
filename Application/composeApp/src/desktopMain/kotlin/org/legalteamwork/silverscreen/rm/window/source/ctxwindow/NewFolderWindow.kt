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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.rememberCursorPositionProvider
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource

@Composable
fun ContextWindowScope.NewFolderWindow() {
    val width = 250.dp
    val shape = RoundedCornerShape(5.dp)

    val popupPositionProvider = rememberCursorPositionProvider(
        offset = DpOffset(5.dp, 5.dp),
        alignment = Alignment.TopStart
    )

    Popup(
        popupPositionProvider = popupPositionProvider,
        onDismissRequest = { onContextWindowClose() },
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .wrapContentHeight()
                .shadow(5.dp, shape = shape)
                .background(Color.DarkGray, shape = shape)
                .border(1.dp, Color.LightGray, shape = shape)
        ) {
            Column {
                var folderName by remember { mutableStateOf("New folder") }

                BasicTextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    textStyle = TextStyle(color = Color.White),
                    cursorBrush = SolidColor(Color.Blue)
                ) { innerContent ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(10.dp)
                            .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                            .background(color = Color.Black, shape = RoundedCornerShape(5.dp))
                    ) {
                        Box(Modifier.padding(10.dp)) { innerContent() }
                    }
                }

                Box (modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    Button(
                        onClick = {
                            val folderResource = FolderResource(
                                mutableStateOf(folderName),
                                ResourceManager.videoResources.value,
                                mutableStateListOf()
                            )
                            ResourceManager.addSource(folderResource)
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