package org.legalteamwork.silverscreen.re

import org.legalteamwork.silverscreen.rm.resource.VideoResource
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.rm.window.effects.VideoFilter

/**
 * Состояние видео-эдитора в заданный момент времени
 *
 * @param[timestamp] время в миллисекундах
 */
data class VideoEditorTimeState(val timestamp: Long) {

    /**
     * Находит ресурс, который находится в заданном моменте времени
     */
    private val resourceOnTrack: ResourceOnTrack? = run {
        val frame = timestamp * Project.fps / 1000

        VideoTrack.resourcesOnTrack.find {
            it.position <= frame && it.position + it.framesCount > frame
        }
    }

    /**
     * Находит время внутри ресурса по заданному моменту времени
     */
    val resourceOnTrackTimestamp: Long = run {
        if (resourceOnTrack != null) {
            val resourceStartTimestamp = (resourceOnTrack.position - resourceOnTrack.framesSkip) * 1000 / Project.fps
            timestamp - resourceStartTimestamp.toLong()
        } else {
            0
        }
    }

    /**
     * Вытаскивает из [VideoEditor] видео ресурс по данному времени
     */
    val videoResource: VideoResource? = resourceOnTrack?.let {
        VideoEditor.getVideoResources()[it.id]
    }

    val filters: List<VideoFilter> = resourceOnTrack?.filters?.toList() ?: emptyList()
}