package org.legalteamwork.silverscreen.rm.resource

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.legalteamwork.silverscreen.rm.resource.VideoResourceBuilder.BuildException

class VideoResource(
    override val title: String,
    override val previewPath: String,
    val resourcePath: String,
    /**
     * Number of frames in the video resource
     */
    val numberOfFrames: Int
) : Resource {

    /**
     * Gets a frame from the current video resource with the provided index
     */
    fun getFrame(index: Int): ResourceFrame {
        try {
            val frameGrabber = FFmpegFrameGrabber(resourcePath)
            frameGrabber.format = "mp4"
            frameGrabber.frameNumber = 5

            frameGrabber.start()

            val frame = frameGrabber.grabImage() ?: throw Exception("Frame is NULL!")
            if (frame.image == null) throw Exception("Frame Image is NULL!");

            val converter = Java2DFrameConverter()
            val bufferedImage = converter.convert(frame)

            frameGrabber.stop()
            frameGrabber.close()

            return SimpleResourceFrame(bufferedImage)
        } catch (e: FFmpegFrameGrabber.Exception) {
            throw BuildException()
        }
    }

}