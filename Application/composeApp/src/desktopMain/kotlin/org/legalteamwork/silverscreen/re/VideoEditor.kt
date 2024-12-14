package org.legalteamwork.silverscreen.re

import androidx.compose.runtime.mutableStateListOf
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.*
import org.legalteamwork.silverscreen.rm.resource.VideoResource

/**
 * Базовый класс для панели редактирования видео.
 */
@Serializable
object VideoEditor {
    private val logger = KotlinLogging.logger { }
    val videotracks = mutableStateListOf(VideoTrack)

    fun getResourcesOnTrack() = VideoTrack.resourcesOnTrack

    fun getVideoResources() = VideoTrack.videoResources

    fun getTracks() = videotracks

    fun restore(
        savedResourcesOnTrack: List<ResourceOnTrack>,
        savedVideoResource: List<VideoResource>,
    ) {
        logger.info { "Restoring video resources..." }
        VideoTrack.resourcesOnTrack.clear()
        VideoTrack.resourcesOnTrack.addAll(savedResourcesOnTrack)
        VideoTrack.videoResources.clear()
        VideoTrack.videoResources.addAll(savedVideoResource)
        logger.info { "Restoring video resources finished" }
    }
}