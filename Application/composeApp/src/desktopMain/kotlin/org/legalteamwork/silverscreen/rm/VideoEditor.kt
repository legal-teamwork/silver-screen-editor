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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.rm.resource.ResourceFrame
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import kotlin.math.max
import kotlin.math.roundToInt
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import kotlinx.serialization.*
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.resource.SimpleResource
import java.io.File


/**
 * Базовый класс для панели редактирования видео.
 */
@Serializable
object VideoEditor {
    // Constants
    private val TRACK_MAX_HEIGHT = 500.dp
    private val TRACK_MIN_WIDTH = 30.dp

    // Количество Dp в кадре.
    private var DpInFrame by mutableStateOf(1f)

    private var tracks = mutableStateListOf(VideoTrack)

    /**
     * Класс видео дорожки.
     */
    @Serializable
    object VideoTrack {
        var videoResources = mutableStateListOf<VideoResource>()
        var resourcesOnTrack = mutableStateListOf<ResourceOnTrack>()

        @OptIn(ExperimentalSerializationApi::class)
        @Serializer(forClass = ResourceOnTrack::class)
        class ResourceOnTrackSerializer : KSerializer<ResourceOnTrack> {

            override fun serialize(encoder: Encoder, value: ResourceOnTrack) {
                encoder.encodeSerializableValue(IntArraySerializer(), intArrayOf(value.id, value.position, value.framesCount))
            }

            override fun deserialize(decoder: Decoder): ResourceOnTrack {
                val array = decoder.decodeSerializableValue(IntArraySerializer())
                return ResourceOnTrack(null, array[0], array[1], array[2])
            }
        }

        /**
         * Класс ресурса на дорожке. Позиция и размер в кадрах.
         */
        @Serializable(with = ResourceOnTrackSerializer::class)
        class ResourceOnTrack(@Transient val track: VideoTrack? = null, val id: Int, var position: Int, val framesCount: Int) {
            //private val color = Color(0xFF93C47D - (0x00000001..0x00000030).random() - (0x00000100..0x00003000).random())
            private var localDragTargetInfo = mutableStateOf(DragTargetInfo(position))
            fun getRightBorder(): Int {
                return position + framesCount - 1
            }

            fun getLeftBorder() : Int {
                return position
            }

            fun isPosInside(otherPosition: Int): Boolean {
                return getLeftBorder() <= otherPosition && otherPosition <= getRightBorder()
            }

            fun updatePosition(newPosition: Int) {
                localDragTargetInfo.component1().dragOffset = Offset(newPosition * DpInFrame, 0f)
                position = (localDragTargetInfo.component1().dragOffset.x / DpInFrame).roundToInt()
            }

            fun updateOffset() {
                localDragTargetInfo.component1().dragOffset = Offset(position * DpInFrame, 0f)
            }

            @Composable
            fun <T> dragTarget(
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

                dragTarget(
                    modifier = Modifier.fillMaxHeight().width((framesCount * DpInFrame).dp),
                    dataToDrop = ""
                ) {
                    BoxWithConstraints (
                        modifier = Modifier
                            .fillMaxHeight()
                            .width((framesCount * DpInFrame).dp)
                            .background(color = Color(0xFF93C47D), RoundedCornerShape(20.dp))
                    ) {

                        val textHeight = min(20.dp, maxHeight)
                        val previewHeight = min(75.dp, maxHeight - textHeight)
                        val previewWidth = min(150.dp, minWidth)


                        Column (
                            modifier = Modifier
                                .padding(vertical = 10.dp)

                        ) {
                            Text(
                                text = videoResources[id].title.value,
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
                                contentDescription = videoResources[id].title.value,
                                modifier = Modifier
                                    .width(previewWidth)
                                    .height(previewHeight)
                            )
                        }
                    }
                }
            }
        }


        fun addResource(resource: VideoResource) {
            var pos = 0
            if (resourcesOnTrack.isNotEmpty())
                pos = resourcesOnTrack.maxOf { it.getRightBorder() } + 1
            resourcesOnTrack.add(
                ResourceOnTrack(
                    null,
                    videoResources.size,
                    pos,
                    resource.numberOfFrames
                )
            )
            videoResources.add(resource)
        }

        fun chooseNewPositionAndMoveResources(id: Int, position: Int, framesCount: Int) : Int {
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


        @OptIn(ExperimentalSerializationApi::class)
        @Serializer(forClass = DragTargetInfo::class)
        class LocalDateSerializer : KSerializer<DragTargetInfo> {

            override fun serialize(encoder: Encoder, value: DragTargetInfo) {
                encoder.encodeInt(value.position)
            }

            override fun deserialize(decoder: Decoder): DragTargetInfo {
                return DragTargetInfo(decoder.decodeInt())
            }
        }

        @Serializable
        class DragTargetInfo(var position: Int) {
            var isDragging: Boolean by mutableStateOf(false)
            var dragPosition by mutableStateOf(Offset.Zero)
            var dragOffset by mutableStateOf(Offset(position * DpInFrame, 0f))
            var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
            var dataToDrop by mutableStateOf<Any?>(null)
        }
    }

    fun addResource(resource: Resource) {
        println(resource.title)
        if (resource is VideoResource) {
            tracks[0].addResource(resource)
        }
    }

    fun getResourcesOnTrack() = tracks[0].resourcesOnTrack

    fun getVideoResources() = tracks[0].videoResources

    fun getTracks() = tracks

    /*fun restore(savedTracks: List<VideoTrack>) {
        tracks.clear()
        tracks.addAll(savedTracks)
        println(tracks[0].resourcesOnTrack)
    }*/
    fun restore(savedResourcesOnTrack: List<VideoTrack.ResourceOnTrack>, savedVideoResource: List<VideoResource>) {
        tracks[0].resourcesOnTrack.clear()
        tracks[0].resourcesOnTrack.addAll(savedResourcesOnTrack)
        tracks[0].videoResources.clear()
        tracks[0].videoResources.addAll(savedVideoResource)
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
                        tracks[0].updateResourcesOnTrack()
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
                    tracks[0].compose(adaptiveTrackHeight, this@BoxWithConstraints.maxWidth)
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
