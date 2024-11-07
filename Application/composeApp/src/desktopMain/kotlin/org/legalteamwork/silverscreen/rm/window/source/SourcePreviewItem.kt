package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.decodeToSvgPainter
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindow
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindowData
import java.io.File

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SourcePreviewItem(
    resource: Resource,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    var globalPosition = Offset.Zero

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CELL_PADDING)
            .onGloballyPositioned { layoutCoordinates ->
                globalPosition = layoutCoordinates.positionInParent()
            }
            .onPointerEvent(PointerEventType.Press) { pointerEvent ->
                if (pointerEvent.button == PointerButton.Secondary) {
                    // Right click event
                    val pointerInputChange = pointerEvent.changes[0]
                    val offset = pointerInputChange.position
                    val contextWindowData = ContextWindowData(resource, globalPosition + offset)
                    val contextWindow = ContextWindow(ContextWindow.ContextWindowId.CONTEXT_MENU, contextWindowData)
                    onContextWindowOpen(contextWindow)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        var rememberedTitle by remember { resource.title }

        if (File(resource.previewPath).extension == "svg") {
            Image(
                painter = File(resource.previewPath).inputStream().readAllBytes().decodeToSvgPainter(Density(1f)),
                contentDescription = rememberedTitle,
                modifier = Modifier.size(IMAGE_WIDTH, IMAGE_HEIGHT),
                contentScale = ContentScale.Fit,
                alignment = Alignment.TopCenter
            )
        } else {
            Image(
                painter = BitmapPainter(remember {
                    File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
                }),
                contentDescription = rememberedTitle,
                modifier = Modifier.size(IMAGE_WIDTH, IMAGE_HEIGHT),
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.TopCenter
            )
        }

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
