package org.legalteamwork.silverscreen.render

import org.bytedeco.javacv.FFmpegFrameFilter
import org.bytedeco.javacv.FFmpegFrameGrabber
import java.io.Closeable

data class RecordResourceContext(
    val frameGrabber: FFmpegFrameGrabber,
    val filters: List<FFmpegFrameFilter>
) : Closeable {
    override fun close() {
        filters.reversed().forEach(Closeable::close)
        frameGrabber.close()
    }
}
