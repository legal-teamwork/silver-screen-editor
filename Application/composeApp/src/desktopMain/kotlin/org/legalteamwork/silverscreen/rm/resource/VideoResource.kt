package org.legalteamwork.silverscreen.rm.resource

import org.bytedeco.javacv.FFmpegFrameGrabber

class VideoResource(_title: String, val resourcePath: String) : Resource {

    override val title: String = "$_title : $numberOfFrames"

    /**
     * Path to the resource preview, currently - path in the project resource folder
     * TODO: write better preview support
     */
    override val previewPath: String
        get() {
            // TODO: write better preview making

            return "tmp-resources/flower.jpeg"
        }

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