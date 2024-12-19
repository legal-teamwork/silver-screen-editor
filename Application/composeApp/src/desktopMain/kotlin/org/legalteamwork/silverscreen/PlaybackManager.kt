package org.legalteamwork.silverscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import org.legalteamwork.silverscreen.re.Slider
import kotlin.math.max
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.re.VideoTrack

/**
 * Менеджер воспроизведения видео панели
 */
class PlaybackManager {

    /**
     * Публичное(только на чтение) состояние паузы плеера
     */
    var isPlaying = mutableStateOf(false)
        private set

    /**
     * Публичное(только на чтение) состояние времени в видео, которое мы делаем
     */
    var currentTimestamp = mutableStateOf(0L)
        private set

    /**
     * Приватное время, когда пользователь последний раз нажал воспроизвести
     */
    private var playStartTimestamp: Long = 0

    /**
     * Приватное время, которое рано [currentTimestamp] при последнем запуске пользователем воспроизведения
     */
    private var playStartFromTimestamp: Long = 0

    fun play() {
        playStartTimestamp = System.currentTimeMillis()
        isPlaying.component2().invoke(true)
    }

    fun pause() {
        val currentTimeMillis = System.currentTimeMillis()
        playStartFromTimestamp += currentTimeMillis - playStartTimestamp
        playStartTimestamp = currentTimeMillis
        isPlaying.component2().invoke(false)
    }

    fun playOrPause() {
        if (isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    fun stop() {
        playStartTimestamp = System.currentTimeMillis()
        playStartFromTimestamp = 0
        Slider.updatePosition(0)
        isPlaying.component2().invoke(false)
    }

    fun seek(delta: Long) {
        val currentTimeMillis = System.currentTimeMillis()

        if (isPlaying.value) {
            playStartFromTimestamp = max(playStartFromTimestamp + (currentTimeMillis - playStartTimestamp) + delta, 0)
            playStartTimestamp = currentTimeMillis
        } else {
            playStartFromTimestamp = max(playStartFromTimestamp + delta, 0)
        }
    }

    fun seekToExactPosition(delta: Long) {
        playStartFromTimestamp = delta
    }

    fun seekToExactPositionWhilePlaying(position: Long) {
        val currentTimeMillis = System.currentTimeMillis()
        playStartFromTimestamp = position
        playStartTimestamp = currentTimeMillis
    }

    fun seekToStart() {
        if (isPlaying.value) {
            pause()
        }
        seekToExactPosition(0)
        Slider.updatePosition(0) // Обновляем позицию слайдера
        currentTimestamp.value = 0 //  Также обновляем currentTimestamp
    }

    fun getTotalDuration(): Long {
        val lastFrame = VideoTrack.lengthInFrames
        val fps = Project.get { fps }
        return (lastFrame * 1000 / fps).toLong()
    }

    fun seekToEnd() {
        if (isPlaying.value) {
            pause()
        }

        val totalDuration = getTotalDuration()

        playStartFromTimestamp = totalDuration
        playStartTimestamp = System.currentTimeMillis()
        currentTimestamp.value = totalDuration

        Slider.updatePosition(totalDuration)
    }

    /**
     * Асинхронный запуск бесконечного цикла, сдвигающий ползунок воспроизведения,
     * то есть обновляющий [currentTimestamp]
     */
    suspend fun updateCycle() {
        while (true) {
            currentTimestamp.component2().invoke(calculateCurrentTimestamp())

            Slider.updatePosition(currentTimestamp.value)

            delay(1000L / PLAYBACK_FPS)
        }
    }

    private fun calculateCurrentTimestamp() = if (isPlaying.value) {
        playStartFromTimestamp + (System.currentTimeMillis() - playStartTimestamp)
    } else {
        playStartFromTimestamp
    }

    companion object {
        private const val PLAYBACK_FPS = 25
    }
}

