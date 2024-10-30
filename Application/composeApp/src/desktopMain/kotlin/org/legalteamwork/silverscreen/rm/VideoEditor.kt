package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.rm.resource.ResourceFrame
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Базовый класс для панели редактирования видео.
 */
object VideoEditor {
    // Constants
    private val TRACK_MAX_HEIGHT = 500.dp
    private val TRACK_MIN_WIDTH = 30.dp

    private val tracks = mutableListOf<Track>()

    // Количество Dp в кадре.
    private var DpInFrame = 1f


    /**
     * Класс дорожки.
     */
    class Track {
        inner class ResourceOnTrack(val resource: VideoResource, var position: Int) {

            fun getLeftBorder(): Int {
                return position
            }

            fun getRightBorder(): Int {
                return position + resource.numberOfFrames - 1
            }

            fun isPosInside(pos: Int): Boolean {
                return getLeftBorder() <= pos && pos <= getRightBorder()
            }

            @Composable
            fun compose() {
                var offsetX by remember { mutableStateOf(position * DpInFrame) }

                Box(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), 0) }
                        .fillMaxHeight()
                        .width((resource.numberOfFrames * DpInFrame).dp)
                        .background(color = Color.Magenta, RoundedCornerShape(10.dp))
                        .border(3.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    position = changePosition(position, resource.numberOfFrames, (offsetX / DpInFrame).roundToInt())
                                    offsetX = position * DpInFrame
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetX += dragAmount.x
                                }
                            )
                        }
                )
            }
        }


        private val resources = mutableListOf<ResourceOnTrack>()

        fun deleteByPosition(position: Int) {
            resources.removeIf {it.position == position}
        }

        fun changePosition(position: Int, size: Int, offset: Int) : Int {
            var newPos = max(offset, 0)
            for (res in resources.sortedBy { it.position }) {
                if (res.position == position)
                    continue
                if (res.isPosInside(newPos) ||
                    res.isPosInside(newPos + size - 1) ||
                    (newPos <= res.position && res.position <= newPos + size - 1))
                    newPos = res.getRightBorder() + 1
            }
            return newPos
        }

        fun addResource(res: VideoResource) {
            var pos = 0
            if (resources.isNotEmpty())
                pos = resources.last().getRightBorder() + 1
            resources.add(ResourceOnTrack(res, pos))
        }

        fun getResourceOnPos(position: Int) : ResourceOnTrack? {
            for (res in resources) {
                if (res.isPosInside(position))
                    return res
            }
            return null
        }

        @Composable
        fun compose(trackHeight: Dp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
                    .background(color = Color(0xFF545454), RoundedCornerShape(6.dp))
            ) {
                for (res in resources) {
                    println(res.position)
                    res.compose()
                }
            }
        }
    }

    fun addResource(res: VideoResource, trackId: Int = 0) {
        if (tracks.size > trackId)
            tracks[trackId].addResource(res)
    }

    fun addTrack() {
        tracks.add(Track())
    }

    fun getFrame(position: Int) : ResourceFrame? {
        for (track in tracks) {
            val res = track.getResourceOnPos(position)
            if (res != null && res.resource is VideoResource)
                return res.resource.getFrame(position - res.position)
        }
        return null
    }


    @Composable
    fun compose() {
        BoxWithConstraints(
            modifier = Modifier.background(
                color = Color(0xFF444444),
                shape = RoundedCornerShape(8.dp),
            ).fillMaxSize()
        ) {
            val adaptiveTrackHeight = max(min(maxHeight * 0.3f, TRACK_MAX_HEIGHT), TRACK_MIN_WIDTH)

            Column(
                modifier = Modifier
                    .padding(vertical = maxHeight * 0.05f),
                verticalArrangement = Arrangement.spacedBy(maxHeight * 0.025f)
            ) {
                tracks.forEach { track ->
                    track.compose(adaptiveTrackHeight)
                }
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

