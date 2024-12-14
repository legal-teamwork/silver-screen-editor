package org.legalteamwork.silverscreen.re

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.legalteamwork.silverscreen.rm.window.effects.VideoFilter
import kotlin.math.roundToInt

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
    var framesSkip: Int = 0,
    val filters: SnapshotStateList<VideoFilter> = mutableStateListOf()
) {
    private val logger = KotlinLogging.logger { }
    var framesCount by mutableStateOf(framesCountDefault)
    val localDragTargetInfo = mutableStateOf(DragTargetInfo(position))

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

    fun addFilter(videoFilter: VideoFilter) {
        filters.add(videoFilter)
    }
}