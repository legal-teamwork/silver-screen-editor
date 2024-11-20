package org.legalteamwork.silverscreen

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import kotlin.math.max

class PlaybackManager {

    // Playback timings:
    var isPlaying = mutableStateOf(false)
        private set
    var currentTimestamp = mutableStateOf(0L)
        private set
    private var playStartTimestamp: Long = 0
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
        isPlaying.component2().invoke(false)
    }

    fun stopAndPlay() {
        playStartTimestamp = System.currentTimeMillis()
        playStartFromTimestamp = 0
        isPlaying.component2().invoke(true)
    }

    fun seek(delta: Long) {
        val currentTimeMillis = System.currentTimeMillis()
        playStartFromTimestamp = max(playStartFromTimestamp + (currentTimeMillis - playStartTimestamp) + delta, 0)
        playStartTimestamp = currentTimeMillis
    }

    suspend fun updateCycle() {
        while (true) {
            currentTimestamp.component2().invoke(calculateCurrentTimestamp())

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