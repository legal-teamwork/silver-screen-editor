package org.legalteamwork.silverscreen.vp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.legalteamwork.silverscreen.PlaybackManager
import org.legalteamwork.silverscreen.render.OnlineVideoRenderer
import org.legalteamwork.silverscreen.re.VideoEditorTimeState
import kotlin.math.min
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

object VideoPanel {
    val playbackManager by mutableStateOf(PlaybackManager())

    @Suppress("ktlint:standard:function-naming")
    @Composable
    fun compose() {
        val scope = rememberCoroutineScope()


        playbackManager.stop()
        LaunchedEffect(Unit) {
            scope.launch {
                playbackManager.updateCycle()
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VideoPreview(playbackManager)

        }
    }
}

@Composable
private fun ColumnScope.VideoPreview(playbackManager: PlaybackManager) {
    val onlineVideoRenderer = remember { OnlineVideoRenderer() }
    val currentTimestamp by playbackManager.currentTimestamp
    val timeState = VideoEditorTimeState(currentTimestamp)

    Box(Modifier.Companion.weight(1f).fillMaxWidth()) {
        // Draw canvas
        Canvas(Modifier.fillMaxSize()) {
            val drawScope = this
            drawRect(Color.Black)

            timeState.videoResource?.let { videoResource ->
                // Get drawing frame:
                val videoResourceTimestamp = timeState.resourceOnTrackTimestamp
                if (videoResource != onlineVideoRenderer.videoResource) {
                    onlineVideoRenderer.setVideoResource(videoResource)
                }

                if (timeState.filters != onlineVideoRenderer.filters) {
                    onlineVideoRenderer.setVideoFilters(timeState.filters)
                }

                val bufferedImage = onlineVideoRenderer.grabBufferedVideoFrameByTimestamp(videoResourceTimestamp)
                val imageBitmap = bufferedImage?.toComposeImageBitmap()

                // Draw frame:
                if (imageBitmap != null) {
                    val imageWidth = imageBitmap.width
                    val imageHeight = imageBitmap.height
                    val scale = min(drawScope.size.width / imageWidth, drawScope.size.height / imageHeight)
                    val scaledImageWidth = scale * imageWidth
                    val scaledImageHeight = scale * imageHeight
                    val left = (drawScope.size.width - scaledImageWidth) / 2 / scale
                    val top = (drawScope.size.height - scaledImageHeight) / 2 / scale

                    withTransform(
                        {
                            scale(scale, scale, Offset.Zero)
                            translate(left, top)
                        }
                    ) {
                        drawImage(image = imageBitmap)
                    }
                }
            }
        }
    }
}

private fun formatTime(elapsedTime: Long): String {
    val hours = (elapsedTime / 3600000) % 60
    val minutes = (elapsedTime / 60000) % 60
    val seconds = (elapsedTime / 1000) % 60
    val milliseconds = (elapsedTime % 1000) / 10

    return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
}
