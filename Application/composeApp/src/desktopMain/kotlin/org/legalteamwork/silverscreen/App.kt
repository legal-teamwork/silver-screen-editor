@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.legalteamwork.silverscreen

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
import org.legalteamwork.silverscreen.menu.MenuBarCompose
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.rm.EditingPanel
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.vp.VideoPanel

@Suppress("ktlint:standard:function-naming")
@Composable
fun App() {
    var panelSize by remember { mutableStateOf(Size.Zero) }

    var width1 by remember { mutableStateOf(0.4f) }
    var width2 by remember { mutableStateOf(0.6f) }
    var width3 by remember { mutableStateOf(1f) }
    var height1 by remember { mutableStateOf(0.7f) }
    var height3 by remember { mutableStateOf(0.3f) }

    Surface(color = Color.Black) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
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
                MenuBarCompose()

                // Horizontal divider
                Box(modifier = Modifier.background(Color.Black).height(Dimens.MARGIN_SIZE).width(panelSize.width.dp))

                Row(modifier = Modifier.height((panelSize.height * height1).dp - (2 * Dimens.MARGIN_SIZE + Dimens.DIVIDER_SIZE) / 2)) {
                    // Vertical divider:
                    Box(
                        modifier = Modifier.width(Dimens.MARGIN_SIZE).fillMaxHeight().background(Color.Black),
                    )

                    // Resource manager box:
                    Box(
                        modifier =
                            Modifier
                                .width((panelSize.width * width1).dp - (2 * Dimens.MARGIN_SIZE + Dimens.DIVIDER_SIZE) / 2)
                                .fillMaxHeight()
                                .background(Color.DarkGray, RoundedCornerShape(Dimens.WINDOW_CORNER_RADIUS)),
                    ) {
                        ResourceManager.compose()
                    }

                    // Vertical divider:
                    Box(
                        modifier =
                            Modifier
                                .background(Color.Black)
                                .fillMaxHeight()
                                .width(Dimens.DIVIDER_SIZE)
                                .pointerInput(Unit) {
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

                    // Video panel box:
                    Box(
                        modifier =
                            Modifier
                                .width((panelSize.width * width2).dp - (2 * Dimens.MARGIN_SIZE + Dimens.DIVIDER_SIZE) / 2)
                                .fillMaxHeight()
                                .background(Color.DarkGray, RoundedCornerShape(Dimens.WINDOW_CORNER_RADIUS)),
                    ) {
                        VideoPanel()
                    }

                    // Vertical divider:
                    Box(
                        modifier = Modifier.width(Dimens.MARGIN_SIZE).fillMaxHeight().background(Color.Black),
                    )
                }

                // Horizontal divider:
                Box(
                    modifier =
                        Modifier.background(Color.Black).height(Dimens.DIVIDER_SIZE).fillMaxWidth().pointerInput(Unit) {
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

                Row(modifier = Modifier.height((panelSize.height * height3).dp - 20.dp)) {
                    // Vertical divider:
                    Box(
                        modifier = Modifier.width(Dimens.MARGIN_SIZE).fillMaxHeight().background(Color.Black),
                    )

                    // Video editor box:
                    Box(
                        modifier =
                            Modifier
                                .width((panelSize.width * width3).dp - 2 * Dimens.DIVIDER_SIZE)
                                .fillMaxHeight()
                                .background(Color.DarkGray, RoundedCornerShape(Dimens.WINDOW_CORNER_RADIUS)),
                    ) {
                        EditingPanel()
                    }

                    // Vertical divider:
                    Box(
                        modifier = Modifier.width(Dimens.MARGIN_SIZE).fillMaxHeight().background(Color.Black),
                    )
                }

                Box(modifier = Modifier.background(Color.Black).height(Dimens.MARGIN_SIZE).width(panelSize.width.dp))
            }
        }
    }
}
