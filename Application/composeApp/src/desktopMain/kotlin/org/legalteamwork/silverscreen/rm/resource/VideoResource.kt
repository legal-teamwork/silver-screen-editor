package org.legalteamwork.silverscreen.rm.resource

import org.bytedeco.opencv.global.opencv_imgcodecs.imwrite
import org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_COUNT
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_videoio.VideoCapture
import java.io.File

class VideoResource(_title: String, val resourcePath: String) : Resource {
    override val title = "$_title, $size frames"
    override val previewPath: String
        get() {
            val videoCapture = VideoCapture()

            if (!videoCapture.open(resourcePath)) {
                // Not opened, something happened
                throw RuntimeException("Cannot open resource $resourcePath")
            }

            val frame = Mat()

            if (videoCapture.read(frame)) {
                val tempDirectory = File("previews/")
                val outputFile = File(tempDirectory, "output.png")
                tempDirectory.mkdirs()

                imwrite(outputFile.absolutePath, frame)

                videoCapture.release()

                return outputFile.absolutePath
            } else {
                videoCapture.release()

                throw RuntimeException("Cannot get the first video frame")
            }
        }

    val size: Int
        get() {
            val videoCapture = VideoCapture()

            if (!videoCapture.open(resourcePath)) {
                // Not opened, something happened
                throw RuntimeException("Cannot open resource $resourcePath")
            }

            return videoCapture.get(CAP_PROP_FRAME_COUNT).toInt()
        }

    fun getFrame(index: Int): Frame = TODO()

}