package org.legalteamwork.silverscreen.vp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Your lovely masterpiece", color = Color.White)
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
                    painter = painterResource("buttons/rewind_backwards_button.svg"),
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
                modifier =
                    Modifier
                        .padding(end = 20.dp),
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
                modifier =
                    Modifier
                        .padding(end = 20.dp),
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

private fun formatTime(elapsedTime: Long): String {
    val hours = (elapsedTime / 3600000) % 60
    val minutes = (elapsedTime / 60000) % 60
    val seconds = (elapsedTime / 1000) % 60
    val milliseconds = (elapsedTime % 1000) / 10

    return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
}
