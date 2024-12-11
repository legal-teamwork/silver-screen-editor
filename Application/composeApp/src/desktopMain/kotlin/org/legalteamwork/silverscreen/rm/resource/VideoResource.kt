package org.legalteamwork.silverscreen.rm.resource

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.legalteamwork.silverscreen.menu.logger
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.serializers.MutableStateStringSerializer
import org.legalteamwork.silverscreen.save.Project
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.pathString

@Serializable
class VideoResource(
    val resourcePath: String,
    @Transient
    override var parent: FolderResource? = ResourceManager.rootFolder,
    @Serializable(with = MutableStateStringSerializer::class)
    override val title: MutableState<String> = mutableStateOf(File(resourcePath).name),
    /**
     * Pre-calculated number of frames in the provided video resource
     */
    private val framesCount: Int? = null
) : Resource {
    override val previewPath: String by lazy { buildPreviewFile() }
    private val fileInfo: Pair<Int, Double> by lazy { getVideoInfo() }
    private val frameGrabber: FFmpegFrameGrabber by lazy {
        FFmpegFrameGrabber(resourcePath).apply { start() }
    }
    val numberOfFrames: Int
        get() = framesCount?.let { fromProjectFPS(it) } ?: fileInfo.first
    val framesInProjectFPS: Int
        get() = framesCount ?: toProjectFPS(fileInfo.first)
    val fps: Double
        get() = fileInfo.second
    override val properties: ResourceProperties
        get() = ResourceProperties(
            listOf(
                ResourceProperty("Description", "Resource type", "Video"),
                ResourceProperty("Description", "Title", title.value),
                ResourceProperty("Description", "Preview path", previewPath),
                ResourceProperty("Video", "Resource path", resourcePath),
                ResourceProperty("Video", "Number of frames", numberOfFrames.toString()),
                ResourceProperty("Video", "FPS", fps.toString())
            )
        )

    constructor(resourceFile: File, parent: FolderResource?, framesCount: Int? = null) : this(
        resourceFile.absolutePath, resourceFile.name, parent, framesCount
    )

    constructor(resourcePath: String, stringTitle: String, parent: FolderResource?, framesCount: Int? = null) : this(
        resourcePath, parent, mutableStateOf(stringTitle), framesCount
    )

    /**
     * Returns amount of frames and frame rate in the provided resource file
     *
     * @see FFmpegFrameGrabber.getLengthInVideoFrames
     */
    fun getVideoInfo(): Pair<Int, Double> {
        try {
            val lengthInFrames = frameGrabber.lengthInFrames
            val fps = frameGrabber.frameRate

            return lengthInFrames to fps
        } catch (e: FFmpegFrameGrabber.Exception) {
            throw BuildException()
        }
    }

    fun toProjectFPS(frames: Int) = (frames * Project.fps / fps).toInt()
    fun fromProjectFPS(frames: Int) = (frames * fps / Project.fps).toInt()
    fun toTimestamp(frames: Int) = (frames * 1000 / fps).toLong()
    fun fromTimestamp(timestamp: Long) = (timestamp * fps / 1000).toInt()

    /**
     * Gets a frame from the current video resource with the provided index.
     * Too slow because of the line `frameGrabber.frameNumber = index`
     */
    fun getFrame(index: Int = 5): ResourceFrame {
        try {
            frameGrabber.frameNumber = index
            val frame = frameGrabber.grabImage() ?: throw Exception("Frame is NULL!")
            if (frame.image == null) throw Exception("Frame Image is NULL!")

            val converter = Java2DFrameConverter()
            val bufferedImage = converter.convert(frame)

            return ResourceFrame(bufferedImage)
        } catch (e: FFmpegFrameGrabber.Exception) {
            throw BuildException()
        }
    }

    /**
     * Gets a frame from the current video resource with the provided timestamp.
     * Too slow because of the line `frameGrabber.frameNumber = (timestamp * frameGrabber.frameRate / 1000).toInt()`
     */
    fun getFrameByTimestamp(timestamp: Long): ResourceFrame {
        return getFrame(fromTimestamp(timestamp))
    }

    /**
     * Creates preview for the current video resource
     *
     * @return preview file path
     */
    fun buildPreviewFile(): String {
        logger.info { "buildPreviewFile called" }
        val result = kotlin.io.path.createTempFile().pathString
        val bufferedImage = getFrame().bufferedImage
        val previewFile = File(result)

        // Scale to size with width = 256
        val scaledInstance = bufferedImage.getScaledInstance(256, -1, java.awt.Image.SCALE_FAST)
        val scaledBufferedImage = BufferedImage(
            scaledInstance.getWidth(null), scaledInstance.getHeight(null), BufferedImage.TYPE_INT_ARGB
        )
        val graphics = scaledBufferedImage.createGraphics()
        graphics.drawImage(scaledInstance, 0, 0, null)
        graphics.dispose()
        ImageIO.write(scaledBufferedImage, "png", previewFile)

        return result
    }

    override fun clone() = VideoResource(resourcePath, "${title.value} (clone)", parent)
    override fun action() {}

    class BuildException : Exception()
}
