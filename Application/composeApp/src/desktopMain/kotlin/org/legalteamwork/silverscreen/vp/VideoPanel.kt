package org.legalteamwork.silverscreen.vp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text

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

            // Ползунок для видео
            Slider(
                value = playbackManager.currentTimestamp.value.toFloat(),
                onValueChange = { newValue -> playbackManager.seek(newValue.toLong()) },
                valueRange = 0f..playbackManager.totalDuration.value.toFloat(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Панель времени
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = formatTime(playbackManager.currentTimestamp.value),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                // Центральные кнопки управления
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { playbackManager.seekToStart() }) {
                        Image(
                            painter = painterResource("player-control-panel-buttons/Left Start.svg"),
                            contentDescription = "В начало",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(onClick = { playbackManager.seek(-10_000) }) {
                        Image(
                            painter = painterResource("player-control-panel-buttons/rewind_backwards_button.svg"),
                            contentDescription = "Назад",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(onClick = { playbackManager.playOrPause() }) {
                        Image(
                            painter = painterResource(
                                if (playbackManager.isPlaying.value)
                                    "player-control-panel-buttons/pause_button.svg"
                                else
                                    "player-control-panel-buttons/play_button.svg"
                            ),
                            contentDescription = "Воспроизведение/Пауза",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(onClick = { playbackManager.stop() }) {
                        Image(
                            painter = painterResource("player-control-panel-buttons/stop_button.svg"),
                            contentDescription = "Стоп",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(onClick = { playbackManager.seek(10_000) }) {
                        Image(
                            painter = painterResource("player-control-panel-buttons/rewind_forward_button.svg"),
                            contentDescription = "Вперед",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(onClick = { playbackManager.seekToEnd() }) {
                        Image(
                            painter = painterResource("player-control-panel-buttons/Right End.svg"),
                            contentDescription = "В конец",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Кнопки громкости и полноэкранный режим
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { playbackManager.toggleVolume() }) {
                        Image(
                            painter = painterResource(
                                when {
                                    playbackManager.volume.value < 0.25f -> "player-control-panel-buttons/<25%.svg"
                                    playbackManager.volume.value < 0.75f -> "player-control-panel-buttons/25%><75%.svg"
                                    else -> "player-control-panel-buttons/>75%.svg"
                                }
                            ),
                            contentDescription = "Громкость",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(onClick = { playbackManager.toggleFullscreen() }) {
                        Image(
                            painter = painterResource("player-control-panel-buttons/fullscreen.png"),
                            contentDescription = "Полноэкранный режим",
                            modifier = Modifier.size(24.dp)
                        )
                    }
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

    Box(Modifier.weight(1f).fillMaxWidth()) {
        // Рисуем канвас
        Canvas(Modifier.fillMaxSize()) {
            val drawScope = this
            drawRect(Color.Black)

            timeState.videoResource?.let { videoResource ->
                // Получаем кадр для рисования:
                val videoResourceTimestamp = timeState.resourceOnTrackTimestamp
                if (videoResource != onlineVideoRenderer.videoResource)
                    onlineVideoRenderer.setVideoResource(videoResource)

                val bufferedImage = onlineVideoRenderer.grabBufferedVideoFrameByTimestamp(videoResourceTimestamp)
                val imageBitmap = bufferedImage?.toComposeImageBitmap()

                // Рисуем кадр:
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
