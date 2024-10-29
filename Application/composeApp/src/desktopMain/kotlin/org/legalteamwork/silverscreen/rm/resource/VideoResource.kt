package org.legalteamwork.silverscreen.rm.resource

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.pathString

class VideoResource : Resource {

    override val title: String
    val resourcePath: String
    override val previewPath: String
    val length : Int

    constructor(resourcePath_ : String, title_ : String = File(resourcePath_).name) {
        resourcePath = resourcePath_
        title = title_
        val bufferredImage : BufferedImage = getFrame().bufferedImage
        previewPath = kotlin.io.path.createTempFile().pathString
        val previewFile : File = File(previewPath)
        ImageIO.write(bufferredImage, "png", previewFile)
        length = grabLengthInFrames(File(resourcePath))
    }

    /**
     * Gets a frame from the current video resource with the provided index
     */
    fun getFrame(index: Int = 5): ResourceFrame {
        try {
            val frameGrabber = FFmpegFrameGrabber(resourcePath)
            frameGrabber.format = "mp4"
            frameGrabber.frameNumber = index

            frameGrabber.start()

            val frame = frameGrabber.grabImage() ?: throw Exception("Frame is NULL!")
            if (frame.image == null) throw Exception("Frame Image is NULL!")

            val converter = Java2DFrameConverter()
            val bufferedImage = converter.convert(frame)

            frameGrabber.stop()
            frameGrabber.close()

            return ResourceFrame(bufferedImage)
        } catch (e: FFmpegFrameGrabber.Exception) {
            throw BuildException()
        }
    }

    fun grabLengthInFrames(resourceFile: File): Int {
        val frameGrabber = FFmpegFrameGrabber(resourceFile)

        try {
            frameGrabber.start()
            val lengthInFrames = frameGrabber.lengthInFrames
            frameGrabber.stop()
            frameGrabber.close()

            return lengthInFrames
        } catch (e: FFmpegFrameGrabber.Exception) {
            throw BuildException()
        }
    }

    class BuildException : Exception()

}