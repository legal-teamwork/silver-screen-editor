package org.legalteamwork.silverscreen.rm.resource

import org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_COUNT
import org.bytedeco.opencv.opencv_videoio.VideoCapture

class VideoResource(override val title: String, val resourcePath: String) : Resource {


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
     *
     * @see CAP_PROP_FRAME_COUNT
     */
    val numberOfFrames: Int
        get() {
            val videoCapture = VideoCapture()

            if (!videoCapture.open(resourcePath)) {
                // Not opened, something happened
                // TODO: make pop up window with error description

                throw RuntimeException("Cannot open resource $resourcePath")
            } else {
                return videoCapture.get(CAP_PROP_FRAME_COUNT).toInt()
            }
        }

    /**
     * Gets a frame from the current video resource with the provided index
     */
    fun getFrame(index: Int): Frame = TODO()

}