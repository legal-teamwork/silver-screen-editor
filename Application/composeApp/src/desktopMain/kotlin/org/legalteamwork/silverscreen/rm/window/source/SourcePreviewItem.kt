package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.Resource
import java.io.File

@Composable
fun WithContextMenu(content: @Composable ContextMenuScope.() -> Unit) {
    ContextMenuScope().apply {
        Box(modifier = Modifier.fillMaxSize()) {
            content()

            val contextMenu by remember { contextMenuState }
            contextMenu?.invoke()
        }
    }

}

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun SourcePreviewItem(resource: Resource) {
    WithContextMenu {
        var contextMenu by remember { contextMenuState }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CELL_PADDING)
                .onClick(
                    enabled = true,
                    matcher = PointerMatcher.mouse(PointerButton.Secondary),
                    onClick = {
                        contextMenu = { ResourceActionsMenu(resource, { contextMenu = it }, { contextMenu = null }) }
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var rememberedTitle by remember { resource.title }

            Image(
                painter = BitmapPainter(remember {
                    File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
                }),
                contentDescription = rememberedTitle,
                modifier = Modifier.size(IMAGE_WIDTH, IMAGE_HEIGHT),
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.TopCenter
            )

            BasicTextField(
                value = rememberedTitle,
                onValueChange = { rememberedTitle = it },
                modifier = Modifier
                    .padding(top = 5.dp) // outer padding
                    .wrapContentSize(align = Alignment.BottomCenter)
                    .border(1.dp, SolidColor(Color.Black), RoundedCornerShape(2.dp))
                    .padding(5.dp),
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                cursorBrush = SolidColor(Color.Magenta),
            )
        }
    }
}

@Composable
private fun ResourceActionsMenu(
    resource: Resource,
    openContextMenu: (@Composable () -> Unit) -> Unit = {},
    closeContextMenuAction: () -> Unit = {},
) {
    Surface(
        modifier = Modifier.wrapContentSize().zIndex(1f),
        color = Color.Transparent,
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp,
    ) {
        Column(Modifier.width(200.dp)) {
            // Drag line:
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color.Blue))

            // Main part
            Column(modifier = Modifier.background(Color.DarkGray)) {
                ResourceAction("Clone") {
                    ResourceManager.addSource(resource.clone())
                    closeContextMenuAction()
                }
                ResourceAction("Delete") {
                    ResourceManager.removeSource(resource)
                    closeContextMenuAction()
                }
                ResourceAction("Properties") {
                    openContextMenu {
                        ResourcePropertiesMenu(resource)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResourcePropertiesMenu(
    resource: Resource
) {
    Surface(
        modifier = Modifier.width(200.dp).wrapContentHeight().offset(y = 50.dp, x = 80.dp),
        shape = RoundedCornerShape(10.dp, 10.dp),
        color = Color.Black,
        elevation = 5.dp,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Properties for the ${resource.title} resource", modifier = Modifier.padding(5.dp))
        }
    }
}

@Composable
private fun ResourceAction(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(text = text, modifier = Modifier.padding(5.dp))
    }
}
