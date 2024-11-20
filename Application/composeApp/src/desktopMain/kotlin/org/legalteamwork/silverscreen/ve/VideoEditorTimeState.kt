package org.legalteamwork.silverscreen.ve

import org.legalteamwork.silverscreen.rm.resource.VideoResource

/**
 * Состояние видео-эдитора в заданный момент времени
 *
 * @param[timestamp] время в миллисекундах
 */
data class VideoEditorTimeState(val timestamp: Long) {

    /**
     * Находит ресурс, который находится в заданном моменте времени
     */
    private val resourceOnTrack: VideoEditor.VideoTrack.ResourceOnTrack? = run {
        val frame = timestamp * VideoEditor.FRAME_RATE / 1000

        VideoEditor.VideoTrack.resourcesOnTrack.find {
            it.position <= frame && it.position + it.framesCount > frame
        }
    }

    /**
     * Находит время внутри ресурса по заданному моменту времени
     */
    val resourceOnTrackTimestamp: Long = run {
        if (resourceOnTrack != null) {
            val resourceStartTimestamp = resourceOnTrack.position * 1000 / VideoEditor.FRAME_RATE
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

}