package org.legalteamwork.silverscreen

import kotlin.math.max

object PlaybackManager {

    var isPlaying: Boolean = false
        private set
    var playStartTimestamp: Long = 0
        private set
    var playStartFromTimestamp: Long = 0
        private set
    val currentTimestamp: Long
        get() = if (isPlaying) {
            playStartFromTimestamp + (System.currentTimeMillis() - playStartTimestamp)
        } else {
            playStartFromTimestamp
        }

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

}