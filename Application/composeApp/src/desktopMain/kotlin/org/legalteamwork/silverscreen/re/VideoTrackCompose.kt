package org.legalteamwork.silverscreen.re

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.edit.MoveResourceOnTrackCommand
import org.legalteamwork.silverscreen.re.VideoEditor.VideoTrack
import org.legalteamwork.silverscreen.re.VideoEditor.VideoTrack.resourcesOnTrack
import org.legalteamwork.silverscreen.re.VideoEditor.VideoTrack.videoResources
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt

private val logger = KotlinLogging.logger {  }

@Composable
fun AppScope.VideoTrackCompose(
    trackHeight: Dp,
    maxWidth: Dp,
) {
    val resources = remember { resourcesOnTrack }
    logger.info { "Composing video resource" }
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .background(color = EditingPanelTheme.VIDEO_TRACK_BACKGROUND_COLOR),
    ) {
        Box(
            modifier =
                Modifier.width(
                    300.dp,
                ).height(
                    trackHeight - 8.dp,
                ).padding(
                    start = 4.dp,
                ).align(
                    Alignment.CenterStart,
                ).background(color = EditingPanelTheme.TRACK_INFO_BACKGROUND_COLOR, RoundedCornerShape(8.dp)),
        ) {
            Column {
                Text(
                    text = String.format("▶ Video Channel"),
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.TopStart).padding(start = 7.dp, top = 7.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 23.sp,
                    color = EditingPanelTheme.TRACK_INFO_TEXT_COLOR,
                )
            }
        }
        Box(modifier = Modifier.width(maxWidth + 304.dp)) {
            for (i in 0..<resources.size) {
                val resourceOnTrackScope = ResourceOnTrackScope(commandManager, resourceManager, resources[i])
                resourceOnTrackScope.ResourceOnTrackCompose()
            }
        }
    }
}

@Composable
private fun VideoEditorMarkup(
    maxWidth: Dp,
    trackHeight: Dp,
    zoom: Float,
) {
    val shortMarkInterval = 10f * DpInFrame
    val longMarkInterval = 100f * DpInFrame
    logger.info { "Markup timeline..." }

    Box(modifier = Modifier.width(maxWidth).height(trackHeight)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            drawRect(color = EditingPanelTheme.VIDEO_TRACK_BACKGROUND_COLOR, size = size)

            for (i in 0 until (width / shortMarkInterval).toInt() + 1) {
                val xPosition = i * shortMarkInterval - 1

                drawLine(
                    color = EditingPanelTheme.SHORT_MARK_INTERVAL_COLOR,
                    start = Offset(xPosition, height * 0.25f),
                    end = Offset(xPosition, height * 0.75f),
                    strokeWidth = 1f,
                )
            }

            for (i in 0 until (width / longMarkInterval).toInt() + 1) {
                val xPosition = i * longMarkInterval - 1

                drawLine(
                    color = EditingPanelTheme.LONG_MARK_INTERVAL_COLOR,
                    start = Offset(xPosition, height * 0.15f),
                    end = Offset(xPosition, height * 0.85f),
                    strokeWidth = 1f,
                )
            }
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
                .offset(x = Dimens.RESOURCES_HORIZONTAL_OFFSET_ON_TRACK)
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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ResourceOnTrackScope.ResourceOnTrackCompose() {
    val size by mutableStateOf(resourceOnTrack.framesCount * DpInFrame * 1.dp)

    DragTarget(
        modifier = Modifier.fillMaxHeight().width(size),
        dataToDrop = "",
    ) {
        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .width(size)
                    .background(color = EditingPanelTheme.DROPPABLE_FILE_BACKGROUND_COLOR, RoundedCornerShape(20.dp)),
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
                            remember {
                                File(videoResources[resourceOnTrack.id].previewPath).inputStream().readAllBytes()
                                    .decodeToImageBitmap()
                            },
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

