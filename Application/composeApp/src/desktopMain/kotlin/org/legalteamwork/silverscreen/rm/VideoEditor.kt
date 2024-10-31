@file:OptIn(ExperimentalFoundationApi::class)

package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
//import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import kotlin.math.max
import kotlin.math.roundToInt
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import java.io.File

/**
 * Базовый класс для панели редактирования видео.
 */
object VideoEditor {
    // Constants
    private val TRACK_MAX_HEIGHT = 500.dp
    private val TRACK_MIN_WIDTH = 30.dp

    // Количество Dp в кадре.
    private var DpInFrame by mutableStateOf(1f)



    /**
     * Класс видео дорожки.
     */
    object VideoTrack {

        private val videoResources = mutableStateListOf<VideoResource>()

        /**
         * Класс ресурса на дорожке. Позиция и размер в кадрах.
         */
        private class ResourceOnTrack(val id: Int, var position: Int, val framesCount: Int) {

            val color = Color(0xFF93C47D - (0x00000001..0x00000030).random() - (0x00000100..0x00003000).random())

            fun getRightBorder(): Int {
                return position + framesCount - 1
            }

            fun getLeftBorder() : Int {
                return position
            }

            fun isPosInside(otherPosition: Int): Boolean {
                return getLeftBorder() <= otherPosition && otherPosition <= getRightBorder()
            }

            var localDragTargetInfo = mutableStateOf(DragTargetInfo(position))

            fun updatePosition(newPosition: Int) {
                localDragTargetInfo.component1().dragOffset = Offset(newPosition * DpInFrame, 0f)
                position = (localDragTargetInfo.component1().dragOffset.x / DpInFrame).roundToInt()
            }

            fun updateOffset() {
                localDragTargetInfo.component1().dragOffset = Offset(position * DpInFrame, 0f)
            }

            @Composable
            fun <T> DragTarget(
                modifier: Modifier,
                dataToDrop: T,
                content: @Composable (() -> Unit)
            ) {

                var currentPosition by remember { mutableStateOf(Offset.Zero) }
                val currentState = localDragTargetInfo.component1()

                Box(modifier = modifier
                    .offset { IntOffset(currentState.dragOffset.x.roundToInt(), 0)}
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
                            currentState.dragOffset = Offset(max(0f, currentState.dragOffset.x + dragAmount.x), 0f)
                        }, onDragEnd = {
                            currentState.isDragging = false
                            position = (currentState.dragOffset.x / DpInFrame).roundToInt()
                            chooseNewPositionAndMoveResources(id, position, framesCount)
                        }, onDragCancel = {
                            currentState.dragOffset = Offset.Zero
                            currentState.isDragging = false
                        })
                    }) {
                    content()
                }
            }

            @OptIn(ExperimentalResourceApi::class)
            @Composable
            fun compose() {


                DragTarget(
                    modifier = Modifier.fillMaxHeight().width((framesCount * DpInFrame).dp),
                    dataToDrop = ""
                ) {
                    BoxWithConstraints (
                        modifier = Modifier
                            .fillMaxHeight()
                            .width((framesCount * DpInFrame).dp)
                            .background(color = color, RoundedCornerShape(20.dp))
                    ) {

                        val textHeight = min(20.dp, maxHeight)
                        val previewHeight = min(75.dp, maxHeight - textHeight)
                        val previewWidth = min(150.dp, minWidth)


                        Column (
                            modifier = Modifier
                                .padding(vertical = 10.dp)

                        ) {
                            Text(

                                text = videoResources[id].title,
                                modifier = Modifier
                                    .offset(x = 10.dp)
                                    .height(textHeight),
                                color = Color.Black,
                            )
                            Image(
                                painter = BitmapPainter(remember {
                                    File(videoResources[id].previewPath).inputStream().readAllBytes()
                                        .decodeToImageBitmap()
                                }),
                                contentDescription = videoResources[id].title,
                                modifier = Modifier
                                    .width(previewWidth)
                                    .height(previewHeight)
                            )
                        }
                    }
                }
            }
        }

        private var resourcesOnTrack = mutableStateListOf<ResourceOnTrack>()

        fun addResource(resource: VideoResource) {
            var pos = 0
            if (resourcesOnTrack.isNotEmpty())
                pos = resourcesOnTrack.maxOf { it.getRightBorder() } + 1
            resourcesOnTrack.add(
                ResourceOnTrack(
                    videoResources.size,
                    pos,
                    resource.numberOfFrames
                )
            )
            videoResources.add(resource)
        }

        private fun chooseNewPositionAndMoveResources(id: Int, position: Int, framesCount: Int) : Int {
            var leftBorder = position
            var rightBorder = leftBorder + framesCount - 1

            val changes = mutableListOf<Pair<Int, Int>>()

            var fl = false
            for (resource in resourcesOnTrack.sortedBy{it.position}) {
                if (resource.id == id) {
                    changes.add(Pair(id, leftBorder))
                    fl = true
                    continue
                }
                if (fl) {
                    if (resource.getLeftBorder() in leftBorder..rightBorder) {
                        changes.add(Pair(resource.id, rightBorder + 1))
                        rightBorder += resource.framesCount
                    }
                }
                else if (leftBorder in resource.getLeftBorder()..resource.getRightBorder()) {
                    leftBorder = resource.getRightBorder() + 1
                    rightBorder = leftBorder + framesCount - 1
                    fl = true
                }
            }
            for (change in changes)
                resourcesOnTrack[resourcesOnTrack.indexOfFirst{it.id == change.first}].updatePosition(change.second)
            for (res in resourcesOnTrack) {
                print(res.position)
                print(" ")
            }
            return leftBorder
        }

        fun updateResourcesOnTrack() {
            for (i in 0..<resourcesOnTrack.size)
                resourcesOnTrack[i].updateOffset()
        }

        @Composable
        fun compose(trackHeight: Dp, maxWidth: Dp) {
            val resources = remember { resourcesOnTrack }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
                    .background(color = Color(0xFF545454), RoundedCornerShape(6.dp))
            ) {
                markup(maxWidth, trackHeight, 50.dp)
                for (i in 0..<resources.size) {
                    resources[i].compose()
                }
            }
        }

        private class DragTargetInfo(position: Int) {
            var isDragging: Boolean by mutableStateOf(false)
            var dragPosition by mutableStateOf(Offset.Zero)
            var dragOffset by mutableStateOf(Offset(position * DpInFrame, 0f))
            var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
            var dataToDrop by mutableStateOf<Any?>(null)
        }
    }

    fun addResource(res: VideoResource) {
        VideoTrack.addResource(res)
    }


    /**
     * Разметка дорожки
     */
    @Composable
    fun markup(maxWidth: Dp, trackHeight: Dp, step: Dp) {

        var position = step * DpInFrame
        while (position < maxWidth) {
            Box (
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x = position, y = -trackHeight * 0.05f)
                    .width(1.dp)
                    .background(color = Color(0xFFCDD3DB))
            )
            Box (
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x = position, y = trackHeight * 0.05f)
                    .width(1.dp)
                    .background(color = Color(0xFFCDD3DB))
            )
            position += step * DpInFrame
        }
    }

    @Composable
    fun compose() {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.5.dp),
            modifier = Modifier
                .background(
                    color = Color.Black)
        ){
            /**
             * Панель с инструментами.
             */
            BoxWithConstraints(
                modifier = Modifier
                    .background(
                        color = Color(0xFF444444),
                        shape = RoundedCornerShape(8.dp))
                    .fillMaxHeight()
                    .width(55.dp)
                    .padding(3.5.dp)
            ) {
                val buttonColors =
                    ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF3A3A3A),
                        contentColor = Color.White,
                        disabledBackgroundColor = Color(0xFF222222),
                        disabledContentColor = Color.White,
                    )
                Button(
                    onClick = {
                        DpInFrame += 0.5f
                        if (DpInFrame > 3)
                            DpInFrame = 0.5f
                        VideoTrack.updateResourcesOnTrack()
                    },
                    colors = buttonColors,

                    modifier = Modifier
                        .size(45.dp)
                ) {
                    Image(
                        painter = painterResource("buttons/lens.png"),
                        contentDescription = "Приблизить/Отдалить дорожку",
                        contentScale = ContentScale.FillWidth
                    )
                }
            }

            /**
             * Панель с дорожками.
             */
            BoxWithConstraints(
                modifier = Modifier
                    .background(
                        color = Color(0xFF444444),
                        shape = RoundedCornerShape(8.dp))
                    .fillMaxSize()
            ) {


                val adaptiveTrackHeight = max(min(maxHeight * 0.45f, TRACK_MAX_HEIGHT), TRACK_MIN_WIDTH)

                Column(
                    modifier = Modifier
                        .padding(vertical = maxHeight * 0.05f),
                    verticalArrangement = Arrangement.spacedBy(maxHeight * 0.025f)
                ) {
                    VideoTrack.compose(adaptiveTrackHeight, this@BoxWithConstraints.maxWidth)
                }


                // Позиция ползунка.
                var markerPosition by remember { mutableStateOf(0) }

                Box(
                    modifier = Modifier
                        .offset { IntOffset(markerPosition, 0) }
                        .fillMaxHeight()
                        .width(3.dp)
                        .background(color = Color.White, RoundedCornerShape(3.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    markerPosition = max(0, markerPosition + dragAmount.x.roundToInt())
                                }
                            )
                        }
                )
            }
        }
    }
}

