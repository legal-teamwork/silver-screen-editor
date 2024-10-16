@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.legalteamwork.silverscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager

@OptIn(ExperimentalFoundationApi::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun App() {
    var panelSize by remember { mutableStateOf(Size.Zero) }

    var width1 by remember { mutableStateOf(0.4f) }
    var width2 by remember { mutableStateOf(0.6f) }
    var width3 by remember { mutableStateOf(1f) }
    var height1 by remember { mutableStateOf(0.7f) }
    var height3 by remember { mutableStateOf(0.3f) }
    val resourceModifier = ResourceManager()

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
                        resourceModifier.compose()
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
                    )

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
