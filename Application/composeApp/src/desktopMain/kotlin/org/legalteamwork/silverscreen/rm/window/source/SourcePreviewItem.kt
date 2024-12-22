package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.*
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.decodeToSvgPainter
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.edit.AddResourceToTrackFabric
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.SourcePreviewItemTheme
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.window.effects.EffectTransferable
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindow
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindowData
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindowId
import java.io.File

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun AppScope.SourcePreviewItem(
    resource: Resource,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    var globalPosition = Offset.Zero

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp))
            .onGloballyPositioned { layoutCoordinates ->
                globalPosition = layoutCoordinates.positionInParent()
            }
            .combinedClickable(
                enabled = true,
                onClickLabel = resource.title.value,
                onClick = {
                    var activeDelegate by resourceManager.activeResource
                    activeDelegate = resource
                },
                onDoubleClick = {
                    // Fast addition to the timeline
                    val position = VideoTrack.getFreePosition()
                    val addResourceCommand = AddResourceToTrackFabric
                        .makeCommand(resource, VideoTrack, position)

                    if (addResourceCommand != null) {
                        commandManager.execute(addResourceCommand)
                    }
                },
            )
            .onPointerEvent(PointerEventType.Press) { pointerEvent -> // OnRightClick
                if (pointerEvent.button == PointerButton.Secondary) {
                    var activeDelegate by resourceManager.activeResource
                    activeDelegate = resource
                    val pointerInputChange = pointerEvent.changes[0]
                    val offset = pointerInputChange.position
                    val contextWindowData = ContextWindowData(globalPosition + offset)
                    val contextWindow = ContextWindow(ContextWindowId.CONTEXT_MENU, contextWindowData)
                    onContextWindowOpen(contextWindow)
                }
            }
            .dragAndDropSource(
                drawDragDecoration = { }
            ) {
                detectDragGestures(
                    onDragStart = { _ ->
                        startTransfer(
                            DragAndDropTransferData(
                                transferable = DragAndDropTransferable(
                                    ResourceTransferable(resource)
                                ),
                                supportedActions = listOf(
                                    DragAndDropTransferAction.Move,
                                )
                            )
                        )
                    },
                    onDrag = { _, _ -> }
                )
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
                    alignment = Alignment.TopCenter,
                    //colorFilter = ColorFilter.tint(Color(0xCCFFFFFF))
                )
            } else {
                Image(
                    painter = BitmapPainter(remember {
                        File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
                    }),
                    contentDescription = rememberedTitle,
                    modifier = Modifier.size(Dimens.IMAGE_WIDTH, Dimens.IMAGE_HEIGHT).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.FillBounds,
                    alignment = Alignment.TopCenter
                )
            }

            BasicTextField(
                value = rememberedTitle,
                onValueChange = { rememberedTitle = it },
                modifier = Modifier
                    .padding(top = 3.dp)
                    .fillMaxWidth(),
                    //.wrapContentSize(align = Alignment.BottomCenter)
                    //.border(1.dp, SolidColor(SourcePreviewItemTheme.RESOURCE_NAME_OUTLINE_COLOR), RoundedCornerShape(2.dp))
                    //.padding(5.dp),
                singleLine = true,
                textStyle = TextStyle(color = SourcePreviewItemTheme.RESOURCE_TEXT_COLOR, textAlign = TextAlign.Left),
                cursorBrush = SolidColor(SourcePreviewItemTheme.RESOURCE_CURSOR_BRUSH),
            )
        }
    }
}
