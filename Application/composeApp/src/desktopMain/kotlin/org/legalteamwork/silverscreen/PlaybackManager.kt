package org.legalteamwork.silverscreen

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import kotlin.math.max

class PlaybackManager {

    // Playback timings:
    private var isPlaying: Boolean = false
    private var playStartTimestamp: Long = 0
    private var playStartFromTimestamp: Long = 0
    var currentTimestamp = mutableStateOf(0L)
        private set

    fun play() {
        assert(!isPlaying)

        playStartTimestamp = System.currentTimeMillis()
        isPlaying = true
    }

    fun pause() {
        assert(isPlaying)

        val currentTimeMillis = System.currentTimeMillis()
        playStartFromTimestamp += currentTimeMillis - playStartTimestamp
        playStartTimestamp = currentTimeMillis
        isPlaying = false
    }

    fun playOrPause() {
        if (isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun stop() {
        playStartTimestamp = System.currentTimeMillis()
        playStartFromTimestamp = 0
        isPlaying = false
    }

    fun stopAndPlay() {
        playStartTimestamp = System.currentTimeMillis()
        playStartFromTimestamp = 0
        isPlaying = true
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

    private fun calculateCurrentTimestamp() = if (isPlaying) {
        playStartFromTimestamp + (System.currentTimeMillis() - playStartTimestamp)
    } else {
        playStartFromTimestamp
    }

    companion object {
        private const val PLAYBACK_FPS = 20
    }

}