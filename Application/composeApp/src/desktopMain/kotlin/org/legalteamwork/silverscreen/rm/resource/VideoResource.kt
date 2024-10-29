package org.legalteamwork.silverscreen.rm.resource

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.pathString

class VideoResource(val resourcePath: String, override val title: String = File(resourcePath).name) : Resource {
    override val previewPath: String by lazy { buildPreviewFile() }
    val numberOfFrames : Int by lazy { grabLengthInFrames(File(resourcePath)) }

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

    /**
     * Returns amount of frames in the provided resource file
     *
     * @see FFmpegFrameGrabber.getLengthInVideoFrames
     */
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

    /**
     * Creates preview for the current video resource
     *
     * @return preview file path
     */
    fun buildPreviewFile(): String {
        val result = kotlin.io.path.createTempFile().pathString
        val bufferedImage = getFrame().bufferedImage
        val previewFile = File(result)

        // Scale to size with width = 256
        val scaledInstance = bufferedImage.getScaledInstance(256, -1, java.awt.Image.SCALE_FAST)
        val scaledBufferedImage = BufferedImage(
            scaledInstance.getWidth(null),
            scaledInstance.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        )
        val graphics = scaledBufferedImage.createGraphics()
        graphics.drawImage(scaledInstance, 0, 0, null)
        graphics.dispose()
        ImageIO.write(scaledBufferedImage, "png", previewFile)

        return result
    }

    class BuildException : Exception()

}