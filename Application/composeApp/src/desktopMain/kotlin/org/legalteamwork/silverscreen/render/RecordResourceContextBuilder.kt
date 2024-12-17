package org.legalteamwork.silverscreen.render

import org.bytedeco.javacv.FFmpegFrameFilter
import org.bytedeco.javacv.FFmpegFrameGrabber

class RecordResourceContextBuilder(
    private val fps: Double
) {
    private var frameGrabber: FFmpegFrameGrabber? = null
    private val filters: MutableList<FFmpegFrameFilter> = mutableListOf()

    fun setFrameGrabber(grabber: FFmpegFrameGrabber): RecordResourceContextBuilder {
        grabber.frameRate = fps
        frameGrabber = grabber
        return this
    }

    fun addFilter(filter: FFmpegFrameFilter): RecordResourceContextBuilder {
        filter.frameRate = fps
        filters.add(filter)
        return this
    }

    fun addFilters(filters: List<FFmpegFrameFilter>): RecordResourceContextBuilder {
        filters.forEach(this::addFilter)
        return this
    }

    fun build(): RecordResourceContext {
        val outputGrabber = frameGrabber ?: throw IllegalArgumentException()
        val outputFilters = filters.toList()
        outputGrabber.start()
        outputFilters.forEach(FFmpegFrameFilter::start)

        return RecordResourceContext(outputGrabber, outputFilters)
    }
}