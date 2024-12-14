package org.legalteamwork.silverscreen.re

import androidx.compose.runtime.*
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.*
import org.legalteamwork.silverscreen.rm.resource.VideoResource

/**
 * Класс видео дорожки.
 */
object VideoTrack {
    private val logger = KotlinLogging.logger { }
    val videoResources = mutableStateListOf<VideoResource>()
    val resourcesOnTrack = mutableStateListOf<ResourceOnTrack>()
    var highlightedResources = mutableListOf<Int>()
    val lengthInFrames: Int
        get() = resourcesOnTrack.maxOfOrNull { it.getRightBorder() } ?: 0

    fun getFreePosition(): Int =
        if (resourcesOnTrack.isEmpty()) { 0 } else { resourcesOnTrack.maxOf(ResourceOnTrack::getRightBorder) + 1 }

    fun addResource(resource: VideoResource, position: Int): ResourceOnTrack {
        logger.debug { "Adding video resource to timeline" }

        val resourceOnTrack = ResourceOnTrack(null, videoResources.size, position, resource.numberOfFrames - 1)
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

    fun getFrameStatus(frameNumber: Int): FrameStatus {
        val resourceOnTrack = resourcesOnTrack.find { it.isPosInside(frameNumber) }
        val videoResource = resourceOnTrack?.let { videoResources[it.id] }

        return FrameStatus(
            frameNumber,
            resourceOnTrack,
            videoResource,
        )
    }

}