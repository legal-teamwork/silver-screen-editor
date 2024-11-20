package org.legalteamwork.silverscreen.vp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.render.OnlineVideoRenderer
import org.legalteamwork.silverscreen.rm.VideoEditor
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


@Suppress("ktlint:standard:function-naming")
@Composable
fun VideoPanel() {
    var isPlaying by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            scope.launch {
                while (isPlaying) {
                    delay(90)
                    elapsedTime += 90
                }
            }
        } else {
            elapsedTime = 0L
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VideoPreview()

        BasicText(text = formatTime(elapsedTime), modifier = Modifier.align(Alignment.Start))

        Row(
            modifier = Modifier.fillMaxWidth().padding(50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    elapsedTime = maxOf(elapsedTime - 10000, 0)
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
                    isPlaying = !isPlaying
                },
                modifier = Modifier.padding(end = 20.dp),
            ) {
                if (isPlaying) {
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
                    isPlaying = false
                    elapsedTime = 0L
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
                    elapsedTime += 10000
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

@Composable
private fun ColumnScope.VideoPreview() {
    Box(Modifier.Companion.weight(1f).fillMaxWidth()) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(Color.Black)

            if (VideoEditor.VideoTrack.videoResources.isNotEmpty()) {
                val imageBitmap = makeVideoPreview()

                if (imageBitmap != null) { drawImage(image = imageBitmap) }
            }
        }
    }
}

private fun makeVideoPreview(): ImageBitmap? {
    val resourceFrame = OnlineVideoRenderer.getVideoFrame(5000) ?: return null
    val bufferedImage = resourceFrame.bufferedImage

    // Scale to size with width = 256
    val scaledInstance = bufferedImage.getScaledInstance(256, -1, java.awt.Image.SCALE_FAST)
    val scaledBufferedImage = BufferedImage(
        scaledInstance.getWidth(null), scaledInstance.getHeight(null), BufferedImage.TYPE_INT_ARGB
    )
    val graphics = scaledBufferedImage.createGraphics()
    graphics.drawImage(scaledInstance, 0, 0, null)
    graphics.dispose()

    return scaledBufferedImage.toImageBitmap()
}

@OptIn(ExperimentalResourceApi::class)
private fun BufferedImage.toImageBitmap(): ImageBitmap {
    val bufferedImage = this
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "png", byteArrayOutputStream)

    return byteArrayOutputStream.toByteArray().decodeToImageBitmap()
}

private fun formatTime(elapsedTime: Long): String {
    val hours = (elapsedTime / 3600000) % 60
    val minutes = (elapsedTime / 60000) % 60
    val seconds = (elapsedTime / 1000) % 60
    val milliseconds = (elapsedTime % 1000) / 10

    return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
}
