package org.legalteamwork.silverscreen.rm.resource

import org.bytedeco.javacv.FFmpegFrameGrabber

class VideoResource(override val title: String, override val previewPath: String, val resourcePath: String) : Resource {

    /**
     * Number of frames in the video resource
     */
    val numberOfFrames: Int
        get() {
            val frameGrabber = FFmpegFrameGrabber(resourcePath)
            
            try {
                frameGrabber.start()
                val result = frameGrabber.lengthInFrames
                frameGrabber.stop()
                frameGrabber.close()

                return result
            } catch (e: FFmpegFrameGrabber.Exception) {
                return -1
            }
        }

    /**
     * Gets a frame from the current video resource with the provided index
     */
    fun getFrame(index: Int): Frame = TODO()

}