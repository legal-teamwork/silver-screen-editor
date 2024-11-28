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

            Row(
                modifier = Modifier.fillMaxWidth().padding(50.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        playbackManager.seek(-10_000)
                    },
                    modifier = Modifier.padding(end = 20.dp),
                ) {
                    Image(
                        painter = painterResource("buttons/rewind_backwards_button.svg"),
                        contentDescription = "Перемотка назад",
                        modifier = Modifier.size(70.dp),
                    )
                }

                Button(
                    onClick = {
                        playbackManager.playOrPause()
                    },
                    modifier = Modifier.padding(end = 20.dp),
                ) {
                    if (playbackManager.isPlaying.value) {
                        Image(
                            painter = painterResource("buttons/pause_button.svg"),
                            contentDescription = "Пауза",
                            modifier = Modifier.size(70.dp),
                        )
                    } else {
                        Image(
                            painter = painterResource("buttons/play_button.svg"),
                            contentDescription = "Запуск",
                            modifier = Modifier.size(70.dp),
                        )
                    }
                }

                Button(
                    onClick = {
                        playbackManager.stop()
                    },
                    modifier = Modifier.padding(end = 20.dp),
                ) {
                    Image(
                        painter = painterResource("buttons/stop_button.svg"),
                        contentDescription = "Стоп",
                        modifier = Modifier.size(70.dp),
                    )
                }

                Button(
                    onClick = {
                        playbackManager.seek(10_000)
                    },
                    modifier = Modifier.padding(end = 20.dp),
                ) {
                    Image(
                        painter = painterResource("buttons/rewind_forward_button.svg"),
                        contentDescription = "Перемотка вперед",
                        modifier = Modifier.size(70.dp),
                    )
                }
            }
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
                if (videoResource != onlineVideoRenderer.videoResource)
                    onlineVideoRenderer.setVideoResource(videoResource)

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

    BasicText(text = formatTime(currentTimestamp), modifier = Modifier.align(Alignment.Start))
}

private fun formatTime(elapsedTime: Long): String {
    val hours = (elapsedTime / 3600000) % 60
    val minutes = (elapsedTime / 60000) % 60
    val seconds = (elapsedTime / 1000) % 60
    val milliseconds = (elapsedTime % 1000) / 10

    return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
}
