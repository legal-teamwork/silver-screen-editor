package org.legalteamwork.silverscreen.re

import androidx.compose.foundation.*
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draganddrop.dragData
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.edit.MoveResourceOnTrackCommand
import org.legalteamwork.silverscreen.re.VideoTrack.resourcesOnTrack
import org.legalteamwork.silverscreen.re.VideoTrack.videoResources
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.rm.window.effects.EffectTransferable
import org.legalteamwork.silverscreen.rm.window.effects.VideoEffect
import java.awt.datatransfer.DataFlavor
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt

private val logger = KotlinLogging.logger {  }

@Composable
fun AppScope.VideoTrackCompose(trackHeight: Dp, timelineLength: Dp) {
    //val resources = remember { resourcesOnTrack }
    logger.info { "Composing video resource" }
    Box(
        modifier =
            Modifier
                .width(timelineLength)
                .height(trackHeight)
                .background(color = EditingPanelTheme.VIDEO_TRACK_BACKGROUND_COLOR),
    ) {
        for (resource in resourcesOnTrack) {
            val resourceOnTrackScope = ResourceOnTrackScope(commandManager, resourceManager, resource)
            resourceOnTrackScope.ResourceOnTrackCompose()
        }
    }
}


@Composable
private fun <T> ResourceOnTrackScope.DragTarget(
    modifier: Modifier,
    dataToDrop: T,
    content: @Composable (() -> Unit)
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = resourceOnTrack.localDragTargetInfo.component1()

    Box(
        modifier =
            modifier
                .offset { IntOffset(currentState.dragOffset.x.roundToInt(), 0) }
                .onGloballyPositioned {
                    currentPosition = it.localToWindow(Offset.Zero)
                }
                .pointerInput(Unit) {
                    detectDragGestures(onDragStart = {
                        currentState.dataToDrop = dataToDrop
                        currentState.isDragging = true
                        currentState.dragPosition = currentPosition + it
                        currentState.draggableComposable = content
                    }, onDrag = { change, dragAmount ->
                        change.consume()
                        logger.debug { "Dragging video resource..." }
                        currentState.dragOffset = Offset(max(0f, currentState.dragOffset.x + dragAmount.x), 0f)
                    }, onDragEnd = {
                        logger.debug { "Dragged video resource successfully" }
                        currentState.isDragging = false

                        val newPosition = (currentState.dragOffset.x / DpInFrame).roundToInt()
                        val moveResourceOnTrackCommand =
                            MoveResourceOnTrackCommand(resourceOnTrack, VideoTrack, newPosition)
                        commandManager.execute(moveResourceOnTrackCommand) // FIXME use application context
                    }, onDragCancel = {
                        logger.warn { "Canceled dragging video resource" }
                        currentState.dragOffset = Offset.Zero
                        currentState.isDragging = false
                    })
                },
    ) {
        content()
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun ResourceOnTrackScope.ResourceOnTrackCompose() {
    val size by mutableStateOf(resourceOnTrack.framesCount * DpInFrame * 1.dp)
    var droppableFileBackgroundColor by mutableStateOf(EditingPanelTheme.DROPPABLE_FILE_BACKGROUND_COLOR_1)
    var showTargetBorder by mutableStateOf(false)
    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            // Highlights the border of a potential drop target
            override fun onStarted(event: DragAndDropEvent) {
                showTargetBorder = true
            }

            override fun onEnded(event: DragAndDropEvent) {
                showTargetBorder = false
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                val dataFlavor = DataFlavor(
                    DataFlavor.javaSerializedObjectMimeType + ";class=org.legalteamwork.silverscreen.rm.window.effects.VideoEffect",
                    "VideoEffect"
                )
                val transferData = event.awtTransferable.getTransferData(dataFlavor)

                return transferData is VideoEffect
            }
        }
    }
    DragTarget(
        modifier = Modifier
            .fillMaxHeight()
            .width(size)
            .border(width = if (showTargetBorder) { 1.dp } else { 0.dp }, color = Color.White)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragAndDropTarget
            ),
        dataToDrop = "",
    ) {
        val colorStops = arrayOf(
            0.0f to droppableFileBackgroundColor,
            0.2f to EditingPanelTheme.DROPPABLE_FILE_BACKGROUND_COLOR_2,
            0.4f to EditingPanelTheme.DROPPABLE_FILE_BACKGROUND_COLOR_3,
            0.6f to EditingPanelTheme.DROPPABLE_FILE_BACKGROUND_COLOR_4,
            0.8f to EditingPanelTheme.DROPPABLE_FILE_BACKGROUND_COLOR_5,
            1f to droppableFileBackgroundColor
        )

        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .width(size)
                    .background(Brush.horizontalGradient(colorStops = colorStops), RoundedCornerShape(5.dp))
                    .clickable(
                        onClick = {
                            if (VideoEditor.highlightResource(resourceOnTrack.id))
                                droppableFileBackgroundColor = EditingPanelTheme.HIGHLIGHTED_DROPPABLE_FILE_BACKGROUND_COLOR
                            else
                                droppableFileBackgroundColor = EditingPanelTheme.DROPPABLE_FILE_BACKGROUND_COLOR_1
                        }
                    ),
        ) {
            val textHeight = min(20.dp, maxHeight)
            val previewHeight = min(75.dp, maxHeight - textHeight)
            val previewWidth = min(150.dp, minWidth)

            Column(
                modifier =
                    Modifier
                        .padding(vertical = 10.dp),
            ) {
                Text(
                    text = videoResources[resourceOnTrack.id].title.value,
                    modifier =
                        Modifier
                            .offset(x = 10.dp)
                            .height(textHeight),
                    color = EditingPanelTheme.DROPPABLE_FILE_TEXT_COLOR,
                )
                Image(
                    painter =
                        BitmapPainter(
                                File(videoResources[resourceOnTrack.id].previewPath).inputStream().readAllBytes()
                                    .decodeToImageBitmap()
                        ),
                    contentDescription = videoResources[resourceOnTrack.id].title.value,
                    modifier =
                        Modifier
                            .width(previewWidth)
                            .height(previewHeight),
                )
            }
        }
    }
}

