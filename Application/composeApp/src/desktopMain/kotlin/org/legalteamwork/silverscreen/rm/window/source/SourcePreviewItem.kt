package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.decodeToSvgPainter
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.SourcePreviewItemTheme
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindow
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindowData
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindowId
import java.io.File

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun SourcePreviewItem(
    resource: Resource,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    var globalPosition = Offset.Zero

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { layoutCoordinates ->
                globalPosition = layoutCoordinates.positionInParent()
            }
            .combinedClickable(
                enabled = true,
                onClickLabel = resource.title.value,
                onLongClickLabel = resource.title.value,
                role = null,
                onLongClick = {
                    VideoEditor.addResource(resource)  // Временное решение
                },
                onDoubleClick = {
                    ResourceManager.activeResource.component2().invoke(resource)
                    resource.action()
                },
                onClick = {
                    ResourceManager.activeResource.component2().invoke(resource)
                },
            )
            .onPointerEvent(PointerEventType.Press) { pointerEvent ->
                if (pointerEvent.button == PointerButton.Secondary) {
                    ResourceManager.activeResource.component2().invoke(resource)
                    val pointerInputChange = pointerEvent.changes[0]
                    val offset = pointerInputChange.position
                    val contextWindowData = ContextWindowData(globalPosition + offset)
                    val contextWindow = ContextWindow(ContextWindowId.CONTEXT_MENU, contextWindowData)
                    onContextWindowOpen(contextWindow)
                }
            },
    ) {
        val activeModifier = if (ResourceManager.activeResource.value == resource) {
            Modifier.border(
                width = 0.dp, color = SourcePreviewItemTheme.ACTIVE_RESOURCE_OUTLINE_COLOR, shape = RoundedCornerShape(5.dp)
            )
        } else {
            Modifier
        }

        Column(
            modifier = activeModifier.padding(Dimens.CELL_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            var rememberedTitle by remember { resource.title }

            if (File(resource.previewPath).extension == "svg") {
                Image(
                    painter = File(resource.previewPath).inputStream().readAllBytes().decodeToSvgPainter(Density(1f)),
                    contentDescription = rememberedTitle,
                    modifier = Modifier.size(Dimens.IMAGE_WIDTH, Dimens.IMAGE_HEIGHT),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.TopCenter
                )
            } else {
                Image(
                    painter = BitmapPainter(remember {
                        File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
                    }),
                    contentDescription = rememberedTitle,
                    modifier = Modifier.size(Dimens.IMAGE_WIDTH, Dimens.IMAGE_HEIGHT),
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
                    .border(1.dp, SolidColor(SourcePreviewItemTheme.RESOURCE_NAME_OUTLINE_COLOR), RoundedCornerShape(2.dp))
                    .padding(5.dp),
                singleLine = true,
                textStyle = TextStyle(color = SourcePreviewItemTheme.RESOURCE_TEXT_COLOR),
                cursorBrush = SolidColor(SourcePreviewItemTheme.RESOURCE_CURSOR_BRUSH),
            )
        }
    }
}
