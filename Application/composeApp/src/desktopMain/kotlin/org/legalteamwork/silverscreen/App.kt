@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.legalteamwork.silverscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun App() {
    var panelSize by remember { mutableStateOf(Size.Zero) }

    // Начальные размеры панелей
    var initialWidth by remember { mutableStateOf(0.dp) }
    var initialHeight by remember { mutableStateOf(0.dp) }

    // Получаем размеры окна приложения
    var width1 by remember { mutableStateOf(0.dp) }
    var height1 by remember { mutableStateOf(0.dp) }
    var width2 by remember { mutableStateOf(0.dp) }
    var width3 by remember { mutableStateOf(0.dp) }
    var height3 by remember { mutableStateOf(0.dp) }

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

                        // На основе размеров окна устанавливаем начальные размеры панелей

                        initialWidth = layoutCoordinates.size.width.dp
                        initialHeight = layoutCoordinates.size.height.dp

                        width1 = initialWidth * 0.4f
                        width2 = initialWidth * 0.6f - 30.dp
                        width3 = initialWidth - 20.dp
                        height1 = initialHeight * 0.7f
                        height3 = initialHeight * 0.3f - 30.dp
                    },
            contentAlignment = Alignment.Center,
        ) {
            Column {
                Box(modifier = Modifier.background(Color.Black).height(10.dp).width(2540.dp))
                Row {
                    Box(modifier = Modifier.background(Color.Black).height(height1.value.dp).width(10.dp))
                    Box(
                        // Здесь реализация жёлтой панели
                        modifier =
                            Modifier
                                .width(width1)
                                .height(height1)
                                .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                    )

                    Box(
                        // Здесь запрятаны хитбоксы для увеличения и уменьшения размера панели
                        modifier =
                            Modifier.background(Color.Black).height(height1.value.dp).width(10.dp).pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    if (dragAmount.x != 1f) {
                                        width1 =
                                            (width1.value.dp + dragAmount.x.dp).coerceIn(
                                                (initialWidth.value.dp) * 0.4f,
                                                (initialWidth.value.dp) * 0.6f,
                                            )
                                        width2 =
                                            (width2.value.dp - dragAmount.x.dp).coerceIn(
                                                (initialWidth.value.dp) * 0.4f - 30.dp,
                                                (initialWidth.value.dp) * 0.6f - 30.dp,
                                            )
                                    }
                                }
                            },
                    )

                    Box( // Здесь реализация красной панели
                        modifier =
                            Modifier
                                .width(width2)
                                .height(height1)
                                .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                    )
                    Box(modifier = Modifier.background(Color.Black).height(height1.value.dp).width(10.dp))
                }

                Box(
                    modifier =
                        Modifier.background(Color.Black).height(10.dp).width(2540.dp).pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                if (dragAmount.y != 1f) {
                                    height1 =
                                        (height1.value.dp + dragAmount.y.dp).coerceIn(
                                            (initialHeight.value.dp) * 0.5f,
                                            (initialHeight.value.dp) * 0.7f,
                                        )
                                    height3 =
                                        (height3.value.dp - dragAmount.y.dp).coerceIn(
                                            (initialHeight.value.dp) * 0.3f - 30.dp,
                                            (initialHeight.value.dp) * 0.5f - 31.dp,
                                        )
                                }
                            }
                        },
                )

                Row {
                    Box(modifier = Modifier.background(Color.Black).height(height3.value.dp).width(10.dp))
                    Box( // Здесь реализация голубой панели
                        modifier =
                            Modifier
                                .width(width3)
                                .height(height3)
                                .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                    )
                    Box(modifier = Modifier.background(Color.Black).height(height3.value.dp).width(10.dp))
                }
                Box(modifier = Modifier.background(Color.Black).height(10.dp).width(2540.dp))
            }
        }
    }
}
