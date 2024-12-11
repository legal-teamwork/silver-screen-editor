@file:OptIn(ExperimentalFoundationApi::class)

package org.legalteamwork.silverscreen.re

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.edit.CutResourceOnTrackCommand
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.vp.VideoPanel
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt

private val logger = KotlinLogging.logger { }

// Количество Dp в кадре.
@Suppress("ktlint:standard:property-naming")
var DpPerSecond by mutableStateOf(30f)
var DpInFrame: Float
    get() = (DpPerSecond / Project.fps).toFloat()
    set(value) { DpPerSecond = (value * Project.fps).toFloat() }

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
                encoder.encodeSerializableValue(IntArraySerializer(), intArrayOf(value.id, value.position, value.framesCount, value.framesSkip))
            }

            override fun deserialize(decoder: Decoder): ResourceOnTrack {
                logger.info { "Deserializing video resource" }
                val array = decoder.decodeSerializableValue(IntArraySerializer())
                return ResourceOnTrack(null, array[0], array[1], array[2], array[3])
            }
        }

        /**
         * Класс ресурса на дорожке. Позиция и размер в кадрах.
         * @param[framesCountDefault] начальный размер ресурса в кадрах (может быть не равен VideoResource.framesCount).
         * @param[framesSkip] cколько кадров пропустить сначала.
         * @property[framesCount] актуальный размер ресурса в кадрах (меняется при Cut)
         */
        @Serializable(with = ResourceOnTrackSerializer::class)
        class ResourceOnTrack(
            @Transient val track: VideoTrack? = null,
            val id: Int,
            var position: Int,
            val framesCountDefault: Int,
            var framesSkip: Int = 0
        ) {
            var framesCount by mutableStateOf(framesCountDefault)

            var localDragTargetInfo = mutableStateOf(DragTargetInfo(position))

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
        }

        fun getFreePosition(): Int =
            if (resourcesOnTrack.isEmpty()) { 0 } else { resourcesOnTrack.maxOf(ResourceOnTrack::getRightBorder) + 1 }

        fun addResource(resource: VideoResource, position: Int): ResourceOnTrack {
            logger.debug { "Adding video resource to timeline" }

            val resourceOnTrack = ResourceOnTrack(null, videoResources.size, position, resource.framesInProjectFPS)
            resourcesOnTrack.add(resourceOnTrack)
            videoResources.add(resource)

            return resourceOnTrack
        }

        fun addResource(resource: VideoResource, position: Int, framesCount: Int, framesSkip: Int): ResourceOnTrack {
            logger.debug { "Adding video resource to timeline" }

            val resourceOnTrack = ResourceOnTrack(null, videoResources.size, position, framesCount, framesSkip)
            resourcesOnTrack.add(resourceOnTrack)
            videoResources.add(resource)

            return resourceOnTrack
        }

        fun removeResource(resourceOnTrack: ResourceOnTrack) {
            logger.debug { "Removing video resource from the timeline" }

            resourcesOnTrack.remove(resourceOnTrack)
        }

        fun updateResourcesOnTrack() {
            logger.info { "Updating video offsets" }
            for (i in 0..<resourcesOnTrack.size)
                resourcesOnTrack[i].updateOffset()
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

    fun getResourcesOnTrack() = VideoTrack.resourcesOnTrack

    fun getVideoResources() = VideoTrack.videoResources

    fun getTracks() = videotracks

    fun restore(
        savedResourcesOnTrack: List<VideoTrack.ResourceOnTrack>,
        savedVideoResource: List<VideoResource>,
    ) {
        logger.info { "Restoring video resources..." }
        VideoTrack.resourcesOnTrack.clear()
        VideoTrack.resourcesOnTrack.addAll(savedResourcesOnTrack)
        VideoTrack.videoResources.clear()
        VideoTrack.videoResources.addAll(savedVideoResource)
        VideoTrack.resourcesOnTrack.forEach { it.updateOffset() }
        logger.info { "Restoring video resources finished" }
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
                        .background(color = EditingPanelTheme.AUDIO_TRACK_BACKGROUND_COLOR),
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
                            text = String.format("▶ Audio Channel"),
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
            AudioTrack.addResource(resource)
        }
    }

    fun getResourcesOnTrack() = AudioTrack.resourcesOnTrack

    fun getVideoResources() = AudioTrack.audioResources

    fun getTracks() = audiotracks

    fun restore(
        savedResourcesOnTrack: List<AudioTrack.ResourceOnTrack>,
        savedVideoResource: List<VideoResource>,
    ) {
        logger.info { "Restoring audio resources..." } // Может, позже пригодится)
        AudioTrack.resourcesOnTrack.clear()
        AudioTrack.resourcesOnTrack.addAll(savedResourcesOnTrack)
        AudioTrack.audioResources.clear()
        AudioTrack.audioResources.addAll(savedVideoResource)
        logger.info { "Restoring audio resources finished" }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppScope.EditingPanel(panelHeight: Dp) {
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

            Column {
                Button(
                    modifier =
                        Modifier
                            .width(80.dp)
                            .height(50.dp)
                            .padding(0.dp),
                    onClick = {
                        logger.info { "Instrumental button clicked" }
                        DpInFrame += 0.25f
                        if (DpInFrame > 2.5f) {
                            DpInFrame = 2.5f
                        }
                        VideoEditor.VideoTrack.updateResourcesOnTrack()
                        AudioEditor.AudioTrack.updateResourcesOnTrack()
                    },
                    colors = buttonColors,
                ) {
                    Text(
                        text = String.format("+"),
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }

                Button(
                    modifier =
                        Modifier
                            .width(80.dp)
                            .height(55.dp)
                            .padding(top = 5.dp),
                    onClick = {
                        logger.info { "Instrumental button clicked" }
                        DpInFrame -= 0.25f
                        if (DpInFrame < 0.75f) {
                            DpInFrame = 0.75f
                        }
                        VideoEditor.VideoTrack.updateResourcesOnTrack()
                        AudioEditor.AudioTrack.updateResourcesOnTrack()
                    },
                    colors = buttonColors,
                ) {
                    Text(
                        text = String.format("-"),
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }

                Button(
                    modifier =
                    Modifier
                        .width(80.dp)
                        .height(55.dp)
                        .padding(top = 5.dp),
                    onClick = {
                        logger.info { "Cut button clicked" }
                        if (VideoPanel.playbackManager.isPlaying.value)
                            VideoPanel.playbackManager.pause()
                        val cutResourceOnTrackCommand =
                            CutResourceOnTrackCommand(VideoEditor.VideoTrack, Slider.getPosition())
                        commandManager.execute(cutResourceOnTrackCommand)
                    },
                    colors = buttonColors,
                ) {
                    Text(
                        text = String.format("cut"),
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }
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
            val distance = Project.fps * DpInFrame * 5.dp

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 304.dp)
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val currentTimestamp = (tapOffset.x * 1000 / (Project.fps * DpInFrame)).toLong()
                        Slider.updatePosition(currentTimestamp)
                        if (VideoPanel.playbackManager.isPlaying.value) {
                            VideoPanel.playbackManager.seekToExactPositionWhilePlaying(currentTimestamp)
                        }
                        else {
                            VideoPanel.playbackManager.seekToExactPosition(currentTimestamp)
                        }
                    }
                },
            ) {
                Row {
                    for (i in 0 until (this@BoxWithConstraints.maxWidth / distance).toInt() + 1) {
                        Box(modifier = Modifier.width(distance).height(45.dp)) {
                            Column {
                                Box(modifier = Modifier.width(distance).height(25.dp)) {
                                    Box(modifier = Modifier.width(2.dp).height(25.dp).background(Color.White))
                                    if (i * 5 < 60) {
                                        Text(
                                            text = String.format("%ds", i * 5),
                                            fontSize = 15.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(start = 8.dp),
                                        )
                                    } else {
                                        Text(
                                            text = String.format("%dm %ds", (i * 5) / 60, (i * 5) % 60),
                                            fontSize = 15.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(start = 8.dp),
                                        )
                                    }
                                }
                                Box(modifier = Modifier.width(distance).height(20.dp)) {
                                    Row {
                                        for (i in 1..5) {
                                            Row {
                                                Box(modifier = Modifier.width(2.dp).height(20.dp).background(Color.White))
                                                Box(
                                                    modifier =
                                                        Modifier.width(
                                                            (distance - 10.dp) / 5,
                                                        ).height(20.dp).background(EditingPanelTheme.TRACKS_PANEL_BACKGROUND_COLOR),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            val tracksAmount = 2
            val adaptiveAudioTrackHeight = (panelHeight - 110.dp) / tracksAmount
            val adaptiveVideoTrackHeight = (panelHeight - 110.dp) / tracksAmount

            Column(
                modifier =
                    Modifier
                        .padding(top = 55.dp).height(panelHeight - 100.dp),
            ) {
                VideoTrackCompose(adaptiveVideoTrackHeight, this@BoxWithConstraints.maxWidth)
                Box(modifier = Modifier.fillMaxWidth().height(10.dp))
                AudioEditor.AudioTrack.compose(adaptiveAudioTrackHeight, this@BoxWithConstraints.maxWidth)
            }

            Box(modifier = Modifier.padding(start = 304.dp)) {
                Slider.compose(panelHeight - 40.dp)
            }
        }
    }
    logger.info { "Timeline created!" }
}
