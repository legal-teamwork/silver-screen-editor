package org.legalteamwork.silverscreen.save

data class Resolution(
    val width: Int,
    val height: Int,
    val repr: String = "${width}x${height}"
) {
    override fun toString() = repr

    companion object {
        val available = listOf(
            Resolution(640, 480, "640x480 (SD; 480p; 4:3)"),
            Resolution(1280, 720, "1280x720 (HD; 720p; 16:9)"),
            Resolution(1920, 1080, "1920x1080 (Full HD; 1080p; 16:9)"),
            Resolution(2560, 1440, "2560x1440 (Quad HD; 1440p; 16:9)"),
            Resolution(3840, 2160, "3840x2160 (Ultra HD; 4K; 16:9)"),
            Resolution(7680, 4320, "7680x4320 (Full Ultra HD; 8K; 16:9)")
        )
        const val default = 2
    }
}