@file:OptIn(ExperimentalFoundationApi::class)

package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.*
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.rm.AudioEditor.audiotracks
import org.legalteamwork.silverscreen.rm.VideoEditor.videotracks
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt

private val logger = KotlinLogging.logger {  }

// Количество Dp в кадре.
@Suppress("ktlint:standard:property-naming")
var DpInFrame by mutableStateOf(1f)

/**
 * Базовый класс для панели редактирования видео.
 */
@Serializable
object VideoEditor {

    var videotracks = mutableStateListOf(VideoTrack)

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
            override fun serialize(
                encoder: Encoder,
                value: ResourceOnTrack,
            ) {
                logger.info { "Serializing video resource" }
                encoder.encodeSerializableValue(IntArraySerializer(), intArrayOf(value.id, value.position, value.framesCount))
            }

            override fun deserialize(decoder: Decoder): ResourceOnTrack {
                logger.info { "Deserializing video resource" }
                val array = decoder.decodeSerializableValue(IntArraySerializer())
                return ResourceOnTrack(null, array[0], array[1], array[2])
            }
        }

        /**
         * Класс ресурса на дорожке. Позиция и размер в кадрах.
         */
        @Serializable(with = ResourceOnTrackSerializer::class)
        class ResourceOnTrack(
            @Transient val track: VideoTrack? = null,
            val id: Int,
            var position: Int,
            val framesCount: Int,
        ) {
            // private val color = Color(0xFF93C47D - (0x00000001..0x00000030).random() - (0x00000100..0x00003000).random())
            private var localDragTargetInfo = mutableStateOf(DragTargetInfo(position))

            fun getRightBorder(): Int {
                return position + framesCount - 1
            }

            fun getLeftBorder(): Int {
                return position
            }

            fun isPosInside(otherPosition: Int): Boolean {
                return getLeftBorder() <= otherPosition && otherPosition <= getRightBorder()
            }

            fun updatePosition(newPosition: Int) {
                logger.info { "Moving video block..." }
                localDragTargetInfo.component1().dragOffset = Offset(newPosition * DpInFrame, 0f)
                position = (localDragTargetInfo.component1().dragOffset.x / DpInFrame).roundToInt()
            }

            fun updateOffset() {
                logger.info { "Updating offset of video block..." }
                localDragTargetInfo.component1().dragOffset = Offset(position * DpInFrame, 0f)
            }

            @Composable
            fun <T> dragTarget(
                modifier: Modifier,
                dataToDrop: T,
                content: @Composable (() -> Unit),
            ) {
                var currentPosition by remember { mutableStateOf(Offset.Zero) }
                val currentState = localDragTargetInfo.component1()

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
                                    position = (currentState.dragOffset.x / DpInFrame).roundToInt()
                                    chooseNewPositionAndMoveResources(id, position, framesCount)
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
            fun compose() {
                dragTarget(
                    modifier = Modifier.fillMaxHeight().width((framesCount * DpInFrame).dp),
                    dataToDrop = "",
                ) {
                    BoxWithConstraints(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .width((framesCount * DpInFrame).dp)
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
                                text = videoResources[id].title.value,
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
                                            File(videoResources[id].previewPath).inputStream().readAllBytes()
                                                .decodeToImageBitmap()
                                        },
                                    ),
                                contentDescription = videoResources[id].title.value,
                                modifier =
                                    Modifier
                                        .width(previewWidth)
                                        .height(previewHeight),
                            )
                        }
                    }
                }
            }
        }

        fun addResource(resource: VideoResource) {
            var pos = 0
            if (resourcesOnTrack.isNotEmpty()) {
                pos = resourcesOnTrack.maxOf { it.getRightBorder() } + 1
            }
            logger.info { "Adding video resource to timeline" }
            resourcesOnTrack.add(
                ResourceOnTrack(
                    null,
                    videoResources.size,
                    pos,
                    resource.numberOfFrames,
                ),
            )
            videoResources.add(resource)
        }

        fun chooseNewPositionAndMoveResources(
            id: Int,
            position: Int,
            framesCount: Int,
        ): Int {
            var leftBorder = position
            var rightBorder = leftBorder + framesCount - 1

            val changes = mutableListOf<Pair<Int, Int>>()

            var fl = false
            for (resource in resourcesOnTrack.sortedBy { it.position }) {
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
                } else if (leftBorder in resource.getLeftBorder()..resource.getRightBorder()) {
                    leftBorder = resource.getRightBorder() + 1
                    rightBorder = leftBorder + framesCount - 1
                    fl = true
                }
            }
            logger.info { "Repositioning of video resources..." }
            for (change in changes)
                resourcesOnTrack[resourcesOnTrack.indexOfFirst { it.id == change.first }].updatePosition(change.second)
            for (res in resourcesOnTrack) {
                print(res.position)
                print(" ")
            }
            return leftBorder
        }

        fun updateResourcesOnTrack() {
            logger.info { "Updating video offsets" }
            for (i in 0..<resourcesOnTrack.size)
                resourcesOnTrack[i].updateOffset()
        }

        @Composable
        fun compose(
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
                        .background(color = Color(0xFF545454), RoundedCornerShape(6.dp)), //Что за сущность?
            ) {
                markup(maxWidth, trackHeight, 1f)
                for (i in 0..<resources.size) {
                    resources[i].compose()
                }
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        @Serializer(forClass = DragTargetInfo::class)
        class LocalDateSerializer : KSerializer<DragTargetInfo> {
            override fun serialize(
                encoder: Encoder,
                value: DragTargetInfo,
            ) {
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
            videotracks[0].addResource(resource)
        }
    }

    fun getResourcesOnTrack() = videotracks[0].resourcesOnTrack

    fun getVideoResources() = videotracks[0].videoResources

    fun getTracks() = videotracks

    fun restore(
        savedResourcesOnTrack: List<VideoTrack.ResourceOnTrack>,
        savedVideoResource: List<VideoResource>,
    ) {
        logger.info { "Restoring video resources..." }
        videotracks[0].resourcesOnTrack.clear()
        videotracks[0].resourcesOnTrack.addAll(savedResourcesOnTrack)
        videotracks[0].videoResources.clear()
        videotracks[0].videoResources.addAll(savedVideoResource)
        logger.info { "Restoring video resources finished" }
    }

    @Composable
    fun markup(
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
}

/**
 * Базовый класс для панели редактирования аудио.
 */
@Serializable
object AudioEditor {

    var audiotracks = mutableStateListOf(AudioTrack)

    /**
     * Класс аудио дорожки.
     */
    @Serializable
    object AudioTrack {
        var audioResources = mutableStateListOf<VideoResource>()
        var resourcesOnTrack = mutableStateListOf<ResourceOnTrack>()

        @OptIn(ExperimentalSerializationApi::class)
        @Serializer(forClass = ResourceOnTrack::class)
        class ResourceOnTrackSerializer : KSerializer<ResourceOnTrack> {
            override fun serialize(
                encoder: Encoder,
                value: ResourceOnTrack,
            ) {
                logger.info { "Serializing audio" }
                encoder.encodeSerializableValue(IntArraySerializer(), intArrayOf(value.id, value.position, value.framesCount))
            }

            override fun deserialize(decoder: Decoder): ResourceOnTrack {
                logger.info { "Deserializing audio" }
                val array = decoder.decodeSerializableValue(IntArraySerializer())
                return ResourceOnTrack(null, array[0], array[1], array[2])
            }
        }

        /**
         * Класс ресурса на дорожке. Позиция и размер в кадрах.
         */
        @Serializable(with = ResourceOnTrackSerializer::class)
        class ResourceOnTrack(
            @Transient val track: AudioTrack? = null,
            val id: Int,
            var position: Int,
            val framesCount: Int,
        ) {
            // private val color = Color(0xFF93C47D - (0x00000001..0x00000030).random() - (0x00000100..0x00003000).random())
            private var localDragTargetInfo = mutableStateOf(DragTargetInfo(position))

            fun getRightBorder(): Int {
                return position + framesCount - 1
            }

            fun getLeftBorder(): Int {
                return position
            }

            fun isPosInside(otherPosition: Int): Boolean {
                return getLeftBorder() <= otherPosition && otherPosition <= getRightBorder()
            }

            fun updatePosition(newPosition: Int) {
                logger.info { "Updating audio block position" }
                localDragTargetInfo.component1().dragOffset = Offset(newPosition * DpInFrame, 0f)
                position = (localDragTargetInfo.component1().dragOffset.x / DpInFrame).roundToInt()
            }

            fun updateOffset() {
                logger.info { "Updating audio block offset" }
                localDragTargetInfo.component1().dragOffset = Offset(position * DpInFrame, 0f)
            }

            @Composable
            fun <T> dragTarget(
                modifier: Modifier,
                dataToDrop: T,
                content: @Composable (() -> Unit),
            ) {
                var currentPosition by remember { mutableStateOf(Offset.Zero) }
                val currentState = localDragTargetInfo.component1()

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
                                    logger.debug { "Dragging audio resource..." }
                                    currentState.dragOffset = Offset(max(0f, currentState.dragOffset.x + dragAmount.x), 0f)
                                }, onDragEnd = {
                                    logger.debug { "Dragged audio resource successfully" }
                                    currentState.isDragging = false
                                    position = (currentState.dragOffset.x / DpInFrame).roundToInt()
                                    chooseNewPositionAndMoveResources(id, position, framesCount)
                                }, onDragCancel = {
                                    logger.warn { "Canceled dragging audio resource" }
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
            fun compose() {
                dragTarget(
                    modifier = Modifier.fillMaxHeight().width((framesCount * DpInFrame).dp),
                    dataToDrop = "",
                ) {
                    BoxWithConstraints(
                        modifier =
                            Modifier
                                .fillMaxHeight()
                                .width((framesCount * DpInFrame).dp)
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
                                text = audioResources[id].title.value,
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
                                            File(audioResources[id].previewPath).inputStream().readAllBytes()
                                                .decodeToImageBitmap()
                                        },
                                    ),
                                contentDescription = audioResources[id].title.value,
                                modifier =
                                    Modifier
                                        .width(previewWidth)
                                        .height(previewHeight),
                            )
                        }
                    }
                }
            }
        }

        fun addResource(resource: VideoResource) {
            var pos = 0
            if (resourcesOnTrack.isNotEmpty()) {
                pos = resourcesOnTrack.maxOf { it.getRightBorder() } + 1
            }
            logger.info { "Adding audio resource to timeline" }
            resourcesOnTrack.add(
                ResourceOnTrack(
                    null,
                    audioResources.size,
                    pos,
                    resource.numberOfFrames,
                ),
            )
            audioResources.add(resource)
        }

        fun chooseNewPositionAndMoveResources(
            id: Int,
            position: Int,
            framesCount: Int,
        ): Int {
            var leftBorder = position
            var rightBorder = leftBorder + framesCount - 1

            val changes = mutableListOf<Pair<Int, Int>>()

            var fl = false
            for (resource in resourcesOnTrack.sortedBy { it.position }) {
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
                } else if (leftBorder in resource.getLeftBorder()..resource.getRightBorder()) {
                    leftBorder = resource.getRightBorder() + 1
                    rightBorder = leftBorder + framesCount - 1
                    fl = true
                }
            }
            logger.info { "Repositioning of audio resources..." }
            for (change in changes)
                resourcesOnTrack[resourcesOnTrack.indexOfFirst { it.id == change.first }].updatePosition(change.second)
            for (res in resourcesOnTrack) {
                print(res.position)
                print(" ")
            }
            return leftBorder
        }

        fun updateResourcesOnTrack() {
            logger.info { "Updating audio offsets" }
            for (i in 0..<resourcesOnTrack.size)
                resourcesOnTrack[i].updateOffset()
        }

        @Composable
        fun compose(
            trackHeight: Dp,
            maxWidth: Dp,
        ) {
            logger.info { "Composing audio resource" }
            val resources = remember { resourcesOnTrack }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                        .background(color = Color(0xDDE1FFDB), RoundedCornerShape(6.dp)),
            ) {
                markup(maxWidth, trackHeight, 1f)
                for (i in 0..<resources.size) {
                    resources[i].compose()
                }
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        @Serializer(forClass = DragTargetInfo::class)
        class LocalDateSerializer : KSerializer<DragTargetInfo> {
            override fun serialize(
                encoder: Encoder,
                value: DragTargetInfo,
            ) {
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
            audiotracks[0].addResource(resource)
        }
    }

    fun getResourcesOnTrack() = audiotracks[0].resourcesOnTrack

    fun getVideoResources() = audiotracks[0].audioResources

    fun getTracks() = audiotracks

    fun restore(
        savedResourcesOnTrack: List<AudioTrack.ResourceOnTrack>,
        savedVideoResource: List<VideoResource>,
    ) {
        logger.info { "Restoring audio resources..." } //Может, позже пригодится)
        audiotracks[0].resourcesOnTrack.clear()
        audiotracks[0].resourcesOnTrack.addAll(savedResourcesOnTrack)
        audiotracks[0].audioResources.clear()
        audiotracks[0].audioResources.addAll(savedVideoResource)
        logger.info { "Restoring audio resources finished" }
    }

    @Composable
    fun markup(
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

                drawRect(color = EditingPanelTheme.AUDIO_TRACK_BACKGROUND_COLOR, size = size)

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
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun EditingPanel() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.5.dp),
        modifier =
            Modifier
                .background(
                    color = EditingPanelTheme.EDITING_PANEL_BACKGROUND,
                ),
    ) {
        /**
         * Панель с инструментами.
         */
        BoxWithConstraints(
            modifier =
                Modifier
                    .background(
                        color = EditingPanelTheme.TOOL_PANEL_COLOR,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .fillMaxHeight()
                    .width(80.dp)
                    .padding(3.5.dp),
        ) {
            val buttonColors =
                ButtonDefaults.buttonColors(
                    backgroundColor = EditingPanelTheme.TOOL_BUTTONS_BACKGROUND_COLOR,
                    contentColor = EditingPanelTheme.TOOL_BUTTONS_CONTENT_COLOR,
                    disabledBackgroundColor = EditingPanelTheme.TOOL_BUTTONS_DISABLED_BACKGROUND_COLOR,
                    disabledContentColor = EditingPanelTheme.TOOL_BUTTONS_DISABLED_CONTENT_COLOR,
                )

            Button(
                modifier =
                    Modifier
                        .width(80.dp)
                        .height(50.dp)
                        .padding(0.dp),
                onClick = {
                    logger.info { "Instrumental button clicked" }
                    DpInFrame += 0.5f
                    if (DpInFrame > 3) {
                        DpInFrame = 0.5f
                    }
                    videotracks[0].updateResourcesOnTrack()
                    audiotracks[0].updateResourcesOnTrack()
                },
                colors = buttonColors,
            ) {
                if (DpInFrame == 3f) {
                    Text(
                        text = String.format("%.1fx", (DpInFrame - 2.5f)),
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Text(
                        text = String.format("%.1fx", (DpInFrame + 0.5f)),
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }
                    /*
                    Image(
                        painter = painterResource("buttons/lens.png"),
                        contentDescription = "Приблизить/Отдалить дорожку",
                        contentScale = ContentScale.FillWidth
                    )
                     */
            }
        }

        /**
         * Панель с дорожками.
         */
        BoxWithConstraints(
            modifier =
                Modifier
                    .background(
                        color = EditingPanelTheme.TRACKS_PANEL_BACKGROUND_COLOR,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .fillMaxSize(),
        ) {
            val adaptiveAudioTrackHeight = max(min(maxHeight * 0.45f, Dimens.AUDIO_TRACK_MAX_HEIGHT), Dimens.AUDIO_TRACK_MIN_WIDTH)
            val adaptiveVideoTrackHeight = max(min(maxHeight * 0.45f, Dimens.VIDEO_TRACK_MAX_HEIGHT), Dimens.VIDEO_TRACK_MIN_WIDTH)

            Column(
                modifier =
                    Modifier
                        .padding(vertical = maxHeight * 0.05f),
                verticalArrangement = Arrangement.spacedBy(maxHeight * 0.025f),
            ) {
                videotracks[0].compose(adaptiveVideoTrackHeight, this@BoxWithConstraints.maxWidth)
                audiotracks[0].compose(adaptiveAudioTrackHeight, this@BoxWithConstraints.maxWidth)
            }

            var markerPosition by remember { mutableStateOf(0) }

            Box(
                modifier =
                    Modifier
                        .offset { IntOffset(markerPosition, 0) }
                        .fillMaxHeight()
                        .width(3.dp)
                        .background(color = EditingPanelTheme.MARKER_COLOR, RoundedCornerShape(3.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    logger.info { "Dragging to panel..." }
                                    markerPosition = max(0, markerPosition + dragAmount.x.roundToInt())
                                },
                            )
                        },
            )
        }
    }
    logger.info { "Timeline created!" }
}
