package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.findComposeDefaultViewModelStoreOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProviderAtPosition
import androidx.compose.ui.window.PopupProperties
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewFolderWindow(
    contextWindowData: ContextWindowData,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    val resource = contextWindowData.resource
    val position = contextWindowData.position
    val width = 250.dp
    val shape = RoundedCornerShape(5.dp)

    Popup(
        popupPositionProvider = PopupPositionProviderAtPosition(position, true, Offset.Zero, Alignment.BottomEnd, 4),
        onDismissRequest = onContextWindowClose,
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