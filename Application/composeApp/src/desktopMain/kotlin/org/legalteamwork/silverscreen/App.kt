@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.legalteamwork.silverscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import kotlin.math.max
import org.legalteamwork.silverscreen.rm.ResourceManager

@OptIn(ExperimentalFoundationApi::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun App() {
    var width1 by remember { mutableStateOf(900.dp) }
    var height1 by remember { mutableStateOf(740.dp) }
    var width2 by remember { mutableStateOf(1630.dp) }
    var width3 by remember { mutableStateOf(2540.dp) }
    var height3 by remember { mutableStateOf(670.dp) }
    val resourceModifier = ResourceManager()

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black),
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
                ) {
                    resourceModifier.compose()

                    // Здесь запрятаны хитбоксы для увеличения и уменьшения размера панели
                    Box(
                        modifier =
                            Modifier.background(Color.DarkGray).align(Alignment.CenterEnd).height(height1.value.dp - 20.dp).width(8.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        if (dragAmount.x != 1f) {
                                            width1 = (width1.value.dp + dragAmount.x.dp).coerceIn(900.dp, 1265.dp)
                                            width2 = (width2.value.dp - dragAmount.x.dp).coerceIn(1265.dp, 1630.dp)
                                        }
                                    }
                                },
                    )
                    Box(
                        modifier =
                            Modifier.background(Color.DarkGray).align(Alignment.BottomCenter).height(10.dp).width(width1.value.dp - 20.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        if (dragAmount.y != 1f) {
                                            height1 = (height1.value.dp + dragAmount.y.dp).coerceIn(500.dp, 740.dp)
                                            height3 = (height3.value.dp - dragAmount.y.dp).coerceIn(670.dp, 910.dp)
                                        }
                                    }
                                },
                    )
                }

                Box(modifier = Modifier.background(Color.Black).height(height1.value.dp).width(10.dp))

                Box( // Здесь реализация красной панели
                    modifier =
                        Modifier
                            .width(width2)
                            .height(height1)
                            .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                ) {
                    // Здесь запрятаны хитбоксы для увеличения и уменьшения размера панели
                    Box(
                        modifier =
                            Modifier.background(Color(0, 0,0, 0)).align(Alignment.CenterStart).height(height1.value.dp - 20.dp).width(8.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        if (dragAmount.x != 1f) {
                                            width1 = (width1.value.dp + dragAmount.x.dp).coerceIn(900.dp, 1265.dp)
                                            width2 = (width2.value.dp - dragAmount.x.dp).coerceIn(1265.dp, 1630.dp)
                                        }
                                    }
                                },
                    )
                    Box(
                        modifier =
                            Modifier.background(Color.DarkGray).align(Alignment.BottomCenter).height(10.dp).width(width2.value.dp - 20.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        if (dragAmount.y != 1f) {
                                            height1 = (height1.value.dp + dragAmount.y.dp).coerceIn(500.dp, 740.dp)
                                            height3 = (height3.value.dp - dragAmount.y.dp).coerceIn(670.dp, 910.dp)
                                        }
                                    }
                                },
                    )
                }
                Box(modifier = Modifier.background(Color.Black).height(height1.value.dp).width(10.dp))
            }

            Box(modifier = Modifier.background(Color.Black).height(10.dp).width(2540.dp))

            Row {
                Box(modifier = Modifier.background(Color.Black).height(height3.value.dp).width(10.dp))
                Box( // Здесь реализация голубой панели
                    modifier =
                        Modifier
                            .width(width3)
                            .height(height3)
                            .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                ) {
                    val heightCf = 0.16f
                    val widthCf = 1f
                    Box(
                        modifier = Modifier
                            .background(Color.Gray)
                            .height(height3.value.dp * heightCf)
                            .width(width3.value.dp * widthCf)
                            .align(Alignment.Center)
                    ) {
                        var videoWidthCf = 0.05f
                        Column {
                            var topBoxOffset by remember { mutableStateOf(Offset(0f, 0f)) }
                            var topBoxOffsetY by remember { mutableStateOf(Offset(0f, 0f)) }

                            Box(modifier = Modifier
                                .offset {
                                    IntOffset(max(0, topBoxOffset.x.toInt()), topBoxOffset.y.toInt())
                                }

                                .height(height3 * heightCf)
                                .width(width3 * videoWidthCf)
                                .background(Color.Magenta, RoundedCornerShape(8.dp))
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        matcher = PointerMatcher.Primary,
                                        onDragEnd = {
                                            topBoxOffset = topBoxOffsetY
                                        }
                                    ) {
                                        topBoxOffsetY += it.copy(y = 0f)
                                        topBoxOffset += it
                                    }
                                }
                            ) {
                                Text(text = "Video", modifier = Modifier.align(Alignment.Center))
                            }
                        }
                        Column{
                            var markerOffsetY by remember { mutableStateOf(Offset(0f, 0f)) }



                            Box(modifier = Modifier
                                .offset {
                                    IntOffset(max(0, markerOffsetY.x.toInt()), markerOffsetY.y.toInt())
                                }

                                .height(height3 * heightCf)
                                .width(width3 * 0.001f)
                                .background(Color.White)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        matcher = PointerMatcher.Primary,
                                    ) {
                                        markerOffsetY += it.copy(y = 0f)
                                    }
                                }
                            ) {}
                        }
                    }
                    // Здесь запрятан хитбокс для увеличения и уменьшения размера панели
                    Box(
                        modifier =
                            Modifier.background(Color(0, 0,0, 0)).align(Alignment.TopCenter).height(10.dp).width(width3.value.dp - 20.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        if (dragAmount.y != 1f) {
                                            height1 = (height1.value.dp + dragAmount.y.dp).coerceIn(500.dp, 740.dp)
                                            height3 = (height3.value.dp - dragAmount.y.dp).coerceIn(670.dp, 910.dp)
                                        }
                                    }
                                },
                    )
                }
                Box(modifier = Modifier.background(Color.Black).height(height3.value.dp).width(10.dp))
            }
            Box(modifier = Modifier.background(Color.Black).height(10.dp).width(2540.dp))
        }
    }
}
