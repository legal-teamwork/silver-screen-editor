package org.legalteamwork.silverscreen.render

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import java.awt.image.BufferedImage
import java.io.Closeable

/**
 * Простейший рендер класс, необходимый для предпросмотра видео.
 * Оптимизирован под по-кадровый обход последовательно по разным видео ресурсам
 */
class OnlineVideoRenderer : Closeable {

    /**
     * Видеоресурс, который в данный момент мы обходим
     */
    var videoResource: VideoResource? = null
        private set

    /**
     * Логгер
     */
    private val logger = KotlinLogging.logger {}

    /**
     * FFMPEG класс, позволяющий поочередно вытаскивать кадры из видео ресурса
     */
    private var frameGrabber: FFmpegFrameGrabber? = null

    /**
     * Кешированный последний результат получения кадра из [frameGrabber].
     * Нужен, чтобы оптимизировать обход по кадрам, где один кадр может несколько раз повторяться.
     * Например, 1 -> 2 -> 2 -> 2 -> 3 с таким способом только три раза обратится к FFMPEG
     */
    private var cachedPreviousFrame: Frame? = null

    /**
     * Определяет обходимый видео ресурс
     *
     * @param[resource] видео ресурс
     */
    fun setVideoResource(resource: VideoResource) {
        logger.debug { "Changing renderer video resource to: ${resource.title}" }
        // Close previous frameGrabber
        frameGrabber?.let {
            it.stop()
            it.close()
        }

        // Update frameGrabber
        videoResource = resource
        frameGrabber = FFmpegFrameGrabber(resource.resourcePath).apply {
            format = "mp4"
        }.also(FFmpegFrameGrabber::start)
    }

    /**
     * Ютилити функция, определяющая по времени в видео, какой это именно кадр
     *
     * @param[timestamp] время в миллисекундах
     *
     * @return номер кадра
     */
    fun getFrameNumberByTimestamp(timestamp: Long): Int =
        (timestamp * (frameGrabber?.frameRate ?: throw Exception("Invalid method usage")) / 1000).toInt()

    /**
     * Ютилити функция, время начала заданного кадра
     *
     * @param[frameNumber] номер кадра видео
     *
     * @return время в миллисекундах
     */
    fun getTimestampByFrameNumber(frameNumber: Int): Long =
        (frameNumber * 1000 / (frameGrabber?.frameRate ?: throw Exception("Invalid method usage"))).toLong()

    /**
     * Форсированно сдвигает текущий номер кадра к данному числу.
     * Относительно долгая операция.
     *
     * @param[frameNumber] номер кадра
     */
    fun setVideoFrame(frameNumber: Int) {
        frameGrabber?.frameNumber = frameNumber
    }

    /**
     * Форсированно сдвигает текущий номер кадра к кадру, который показывается в заданное время.
     * Относительно долгая операция.
     *
     * @param[timestamp] время в миллисекундах
     *
     * @see OnlineVideoRenderer.getFrameNumberByTimestamp
     * @see OnlineVideoRenderer.setVideoFrame
     */
    fun setVideoFrameByTimestamp(timestamp: Long) {
        frameGrabber?.apply {
            frameNumber = getFrameNumberByTimestamp(timestamp)
        }
    }

    /**
     * Вытаскивает из FFMPEG-а текущий Image Frame
     *
     * @param[frameNumber] номер кадра
     *
     * @return Nullable FFMPEG [Frame] object, null if no frame found
     */
    fun grabVideoFrame(frameNumber: Int): Frame? = frameGrabber?.let { frameGrabber ->
        if (frameGrabber.frameNumber <= frameNumber) {
            while (frameGrabber.frameNumber < frameNumber) {
                logger.debug { "Moving forwards over frame numbers: ${frameGrabber.frameNumber} -> $frameNumber" }

                frameGrabber.grabImage()
            }

            cachedPreviousFrame = frameGrabber.grabImage()
            cachedPreviousFrame
        } else if (frameGrabber.frameNumber == frameNumber + 1) {
            if (cachedPreviousFrame != null) {
                logger.debug { "Using cached frame" }

                cachedPreviousFrame
            } else {
                logger.debug { "Moving backwards over frame numbers: ${frameGrabber.frameNumber} -> $frameNumber" }

                frameGrabber.frameNumber = frameNumber
                cachedPreviousFrame = frameGrabber.grabImage()
                cachedPreviousFrame
            }
        } else {
            logger.debug { "Moving backwards over frame numbers: ${frameGrabber.frameNumber} -> $frameNumber" }

            frameGrabber.frameNumber = frameNumber
            cachedPreviousFrame = frameGrabber.grabImage()
            cachedPreviousFrame
        }
    }

    /**
     * Вытаскивает из FFMPEG-а текущий Image Frame
     *
     * @param[timestamp] время в миллисекундах
     *
     * @return Nullable FFMPEG [Frame] object, null if no frame found
     */
    fun grabVideoFrameByTimestamp(timestamp: Long): Frame? =
        grabVideoFrame(getFrameNumberByTimestamp(timestamp))

    /**
     * Вытаскивает из FFMPEG-а текущий Image Frame и конвертирует его в [java.awt.image.BufferedImage] объект
     *
     * @param[frameNumber] номер кадра
     *
     * @return Nullable [java.awt.image.BufferedImage] object, null if no frame found
     */
    fun grabBufferedVideoFrame(frameNumber: Int): BufferedImage? =
        grabVideoFrame(frameNumber).let { frame ->
            if (frame?.image == null) return@let null
            val converter = Java2DFrameConverter()
            converter.convert(frame)
        }

    /**
     * Вытаскивает из FFMPEG-а текущий Image Frame и конвертирует его в [java.awt.image.BufferedImage] объект
     *
     * @param[timestamp] время в миллисекундах
     *
     * @return Nullable [java.awt.image.BufferedImage] object, null if no frame found
     */
    fun grabBufferedVideoFrameByTimestamp(timestamp: Long): BufferedImage? =
        grabVideoFrameByTimestamp(timestamp).let { frame ->
            if (frame?.image == null) return@let null
            val converter = Java2DFrameConverter()
            converter.convert(frame)
        }

    override fun close() {
        frameGrabber?.let {
            it.stop()
            it.close()
        }
    }

    companion object {
        /**
         * Creates a scaled version of this image.
         * A new [BufferedImage] object is returned which will render
         * the image at the specified [width] and
         * [height] by default. The new [BufferedImage] object
         * may be loaded asynchronously even if the original source image
         * has already been loaded completely.
         *
         * <p>
         *
         * If either [width]
         * or [height] is a negative number then a value is
         * substituted to maintain the aspect ratio of the original image
         * dimensions. If both [width] and [height]
         * are negative, then the original image dimensions are used.
         *
         * @param[width] the width to which to scale the image.
         * @param[height] the height to which to scale the image.
         * @param[hints] flags to indicate the type of algorithm to use for image resampling.
         * @return a scaled version of the image.
         *
         * @throws IllegalArgumentException if [width] or [height] is zero.
         *
         * @see java.awt.Image.SCALE_DEFAULT
         * @see java.awt.Image.SCALE_FAST
         * @see java.awt.Image.SCALE_SMOOTH
         * @see java.awt.Image.SCALE_REPLICATE
         * @see java.awt.Image.SCALE_AREA_AVERAGING
         * @see java.awt.Image.getScaledInstance
         */
        fun scale(bufferedImage: BufferedImage, width: Int = -1, height: Int = -1, hints: Int = java.awt.Image.SCALE_FAST): BufferedImage {
            val scaledInstance = bufferedImage.getScaledInstance(width, height, java.awt.Image.SCALE_FAST)
            val scaledBufferedImage = BufferedImage(
                scaledInstance.getWidth(null),
                scaledInstance.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
            )
            val graphics = scaledBufferedImage.createGraphics()
            graphics.drawImage(scaledInstance, 0, 0, null)
            graphics.dispose()

            return scaledBufferedImage
        }
    }

}