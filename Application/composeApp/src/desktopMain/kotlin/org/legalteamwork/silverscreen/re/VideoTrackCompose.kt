package org.legalteamwork.silverscreen.re

import androidx.compose.foundation.*
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.rm.window.effects.VideoEffect
import org.legalteamwork.silverscreen.rm.window.effects.VideoFilter
import java.awt.datatransfer.DataFlavor
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt

private val logger = KotlinLogging.logger { }

@Composable
fun AppScope.VideoTrackCompose(timelineLength: Dp) {
    //val resources = remember { resourcesOnTrack }
    logger.info { "Composing video resource" }
    Box(
        modifier =
            Modifier
                .width(timelineLength)
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun ResourceOnTrackScope.ResourceOnTrackCompose() {
    val size by mutableStateOf(resourceOnTrack.framesCount * DpInFrame * 1.dp)
    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val dataFlavor = DataFlavor(
                    DataFlavor.javaSerializedObjectMimeType + ";class=org.legalteamwork.silverscreen.rm.window.effects.VideoEffect",
                    "VideoEffect"
                )
                val transferData = event.awtTransferable.getTransferData(dataFlavor)

                if (transferData is VideoEffect) {
                    val videoFilter = transferData.createFilter(resourceOnTrack)
                    resourceOnTrack.addFilter(videoFilter)

                    return true
                } else {
                    return false
                }
            }
        }
    }

    DragTarget(
        modifier = Modifier
            .fillMaxHeight()
            .width(size)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragAndDropTarget
            ),
        dataToDrop = "",
    ) {
        Column {
            ResourceOnTrackMainLine(resourceOnTrack)

            for (videoFilter in resourceOnTrack.filters) {
                ResourceOnTrackFilterLine(videoFilter)
            }
        }
    }
}

@Composable
fun ResourceOnTrackFilterLine(videoFilter: VideoFilter) {
    val videoEffect = videoFilter.videoEffect
    val offset = videoFilter.firstFrame * DpInFrame * 1.dp
    val size = videoFilter.framesLength * DpInFrame * 1.dp

    Box(Modifier.fillMaxWidth().height(Dimens.RESOURCE_ON_TRACK_EFFECT_PART_HEIGHT)) {
        Row {
            Box(Modifier.width(offset))
            Box(
                Modifier
                    .width(size)
                    .fillMaxHeight()
                    .background(EditingPanelTheme.EFFECT_BACKGROUND_COLOR, RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(videoEffect.title)
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ResourceOnTrackMainLine(resourceOnTrack: ResourceOnTrack) {
    var droppableFileBackgroundColor by mutableStateOf(EditingPanelTheme.DROPPABLE_FILE_BACKGROUND_COLOR_1)
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
                .fillMaxWidth()
                .height(Dimens.RESOURCE_ON_TRACK_MAIN_PART_HEIGHT)
                .background(
                    Brush.horizontalGradient(colorStops = colorStops),
                    RoundedCornerShape(5.dp)
                )
                .clickable(
                    onClick = {
                        if (VideoEditor.highlightResource(resourceOnTrack.id))
                            droppableFileBackgroundColor =
                                EditingPanelTheme.HIGHLIGHTED_DROPPABLE_FILE_BACKGROUND_COLOR
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

