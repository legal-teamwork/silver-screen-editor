@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.legalteamwork.silverscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.legalteamwork.silverscreen.rm.ResourceManager

@Suppress("ktlint:standard:function-naming")
@Composable
fun App() {
    var panelSize by remember { mutableStateOf(Size.Zero) }

    var width1 by remember { mutableStateOf(0.4f) }
    var width2 by remember { mutableStateOf(0.6f) }
    var width3 by remember { mutableStateOf(1f) }
    var height1 by remember { mutableStateOf(0.5f) }
    var height3 by remember { mutableStateOf(0.3f) }

    Surface(color = Color.Black) {
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .onGloballyPositioned { layoutCoordinates ->
                        panelSize =
                            Size(
                                layoutCoordinates.size.width.toFloat(),
                                layoutCoordinates.size.height.toFloat(),
                            )
                    },
            contentAlignment = Alignment.Center,
        ) {
            Column {
                Box(modifier = Modifier.background(Color.Black).height(10.dp).width(panelSize.width.dp))

                Row {
                    Box(
                        modifier =
                            Modifier.background(Color.Black).height((panelSize.height * height1).dp - 15.dp)
                                .width(10.dp),
                    )

                    Box(
                        modifier =
                            Modifier
                                .width((panelSize.width * width1).dp - 15.dp)
                                .height((panelSize.height * height1).dp - 15.dp)
                                .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                    ) {
                        ResourceManager.compose()
                    }

                    Box(
                        modifier =
                            Modifier.background(
                                Color.Black,
                            ).height((panelSize.height * height1).dp - 15.dp).width(10.dp).pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val newWidth1 =
                                        (width1 * panelSize.width + dragAmount.x).coerceIn(
                                            panelSize.width * 0.4f,
                                            panelSize.width * 0.6f,
                                        )
                                    width1 = newWidth1 / panelSize.width
                                    width2 = 1 - width1
                                }
                            },
                    )

                    Box(
                        modifier =
                            Modifier
                                .width((panelSize.width * width2).dp - 15.dp)
                                .height((panelSize.height * height1).dp - 15.dp)
                                .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                    ) {
                        VideoPanel()
                    }

                    Box(
                        modifier =
                            Modifier.background(Color.Black).height((panelSize.height * height1).dp - 15.dp)
                                .width(10.dp),
                    )
                }

                Box(
                    modifier =
                        Modifier.background(Color.Black).height(10.dp).width(panelSize.width.dp).pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val newHeight1 =
                                    (height1 * panelSize.height + dragAmount.y).coerceIn(
                                        panelSize.height * 0.5f,
                                        panelSize.height * 0.7f,
                                    )
                                height1 = newHeight1 / panelSize.height
                                height3 = 1 - height1

                            }
                        },
                )

                Row {
                    Box(
                        modifier =
                            Modifier.background(Color.Black).height((panelSize.height * height3).dp - 15.dp)
                                .width(10.dp),
                    )

                    Box(
                        modifier =
                            Modifier
                                .width((panelSize.width * width3).dp - 20.dp)
                                .height((panelSize.height * height3).dp - 15.dp)
                                .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                    )
                    {
                        VideoEditor.compose()
                    }

                    Box(
                        modifier =
                            Modifier.background(Color.Black).height((panelSize.height * height3).dp - 15.dp)
                                .width(10.dp),
                    )
                }

                Box(modifier = Modifier.background(Color.Black).height(10.dp).width(panelSize.width.dp))
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun VideoPanel() {
    var isPlaying by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0 * 0L) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            scope.launch {
                while (isPlaying) {
                    delay(10)
                    elapsedTime += 10
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Ваше видео здесь", color = Color.White)
        }

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
                modifier =
                    Modifier
                        .padding(end = 20.dp),
            ) {
                Image(
                    painter = painterResource("buttons/rewind_backwards_button.png"),
                    contentDescription = "Перемотка назад",
                    modifier = Modifier.size(70.dp),
                )
            }

            Button(
                onClick = {
                    isPlaying = !isPlaying
                },
                modifier =
                    Modifier
                        .padding(end = 20.dp),
            ) {
                if (isPlaying) {
                    Image(
                        painter = painterResource("buttons/pause_button.png"),
                        contentDescription = "Пауза",
                        modifier = Modifier.size(70.dp),
                    )
                } else {
                    Image(
                        painter = painterResource("buttons/play_button.png"),
                        contentDescription = "Запуск",
                        modifier = Modifier.size(70.dp),
                    )
                }
            }

            Button(
                onClick = {
                    isPlaying = false
                    elapsedTime = 0L
                    elapsedTime -= 1
                    elapsedTime = 0L
                },
                modifier =
                    Modifier
                        .padding(end = 20.dp),
            ) {
                Image(
                    painter = painterResource("buttons/stop_button.png"),
                    contentDescription = "Стоп",
                    modifier = Modifier.size(70.dp),
                )
            }

            Button(
                onClick = {
                    elapsedTime += 10000
                },
                modifier =
                    Modifier
                        .padding(end = 20.dp),
            ) {
                Image(
                    painter = painterResource("buttons/rewind_forward_button.png"),
                    contentDescription = "Перемотка вперед",
                    modifier = Modifier.size(70.dp),
                )
            }
        }
    }
}

fun formatTime(elapsedTime: Long): String {
    val hours = (elapsedTime / 3600000) % 60
    val minutes = (elapsedTime / 60000) % 60
    val seconds = (elapsedTime / 1000) % 60
    val milliseconds = (elapsedTime % 1000) / 10

    return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, milliseconds)
}
