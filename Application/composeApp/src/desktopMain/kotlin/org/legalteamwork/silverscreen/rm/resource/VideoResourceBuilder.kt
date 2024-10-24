package org.legalteamwork.silverscreen.rm.resource

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import java.io.File
import javax.imageio.ImageIO


object VideoResourceBuilder {

    fun buildFromFile(resourceFile: File): VideoResource {
        val videoResource = VideoResource(
            resourceFile.name,
            "tmp-resources/flower.jpeg",
            resourceFile.path,
            grabLengthInFrames(resourceFile)
        )

        // TODO: remove this code, what is used to test if frame image can be save to the file:
         val bufferedImage = videoResource.getFrame(5).bufferedImage
         ImageIO.write(bufferedImage, "png", File("output.png"));

        return videoResource
    }

    fun buildFromPath(resourceFilePath: String): VideoResource {
        return buildFromFile(File(resourceFilePath))
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