@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.legalteamwork.silverscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.menu.MenuBarCompose
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.SaveManager
import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.openFileDialog
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
    val marginSize = 6.dp
    val dividerSize = 8.dp
    val windowCornerRadius = 8.dp

    Surface(color = Color.Black) {
        Box(
            modifier = Modifier
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
                Box(modifier = Modifier.background(Color.Black).height(marginSize).width(panelSize.width.dp))

                Row(modifier = Modifier.height((panelSize.height * height1).dp - (2 * marginSize + dividerSize) / 2)) {
                    // Vertical divider:
                    Box(
                        modifier = Modifier.width(marginSize).fillMaxHeight().background(Color.Black),
                    )

                    // Resource manager box:
                    Box(
                        modifier = Modifier
                            .width((panelSize.width * width1).dp - (2 * marginSize + dividerSize) / 2)
                            .fillMaxHeight()
                            .background(Color.DarkGray, RoundedCornerShape(windowCornerRadius)),
                    ) {
                        ResourceManager.compose()
                    }

                    // Vertical divider:
                    Box(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxHeight()
                            .width(dividerSize)
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val newWidth1 = (width1 * panelSize.width + dragAmount.x).coerceIn(
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
                        modifier = Modifier
                            .width((panelSize.width * width2).dp - (2 * marginSize + dividerSize) / 2)
                            .fillMaxHeight()
                            .background(Color.DarkGray, RoundedCornerShape(windowCornerRadius)),
                    ) {
                        VideoPanel()
                    }

                    // Vertical divider:
                    Box(
                        modifier = Modifier.width(marginSize).fillMaxHeight().background(Color.Black),
                    )
                }

                // Horizontal divider:
                Box(
                    modifier = Modifier.background(Color.Black).height(dividerSize).fillMaxWidth().pointerInput(Unit) {
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
                        modifier = Modifier.width(marginSize).fillMaxHeight().background(Color.Black),
                    )

                    // Video editor box:
                    Box(
                        modifier = Modifier
                            .width((panelSize.width * width3).dp - 2 * dividerSize)
                            .fillMaxHeight()
                            .background(Color.DarkGray, RoundedCornerShape(windowCornerRadius)),
                    ) {
                        VideoEditor.compose()
                    }

                    // Vertical divider:
                    Box(
                        modifier = Modifier.width(marginSize).fillMaxHeight().background(Color.Black),
                    )
                }

                Box(modifier = Modifier.background(Color.Black).height(marginSize).width(panelSize.width.dp))
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainButtons() {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val buttonColors =
            ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF3A3A3A),
                contentColor = Color.White,
                disabledBackgroundColor = Color(0xFF222222),
                disabledContentColor = Color.White,
            )

        Button(
            onClick = ResourceManager::addSourceTriggerActivity,
            modifier = Modifier.width(120.dp).height(36.dp).padding(start = 4.dp, top = (2.5).dp),
            colors = buttonColors,
            elevation = null,
            border = null,
        ) {
            Text(
                text = "Import File",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }

        Button(
            onClick = {},
            modifier = Modifier.width(120.dp).height(36.dp).padding(start = 4.dp, top = (2.5).dp),
            colors = buttonColors,
            elevation = null,
            border = null,
        ) {
            Text(
                text = "Export File",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }

        Button(
            onClick = {
                val filenameSet = openFileDialog(null, "Select File", listOf("json"), false)
                if (filenameSet.isNotEmpty()) {
                    SaveManager.load(filenameSet.first().path)
                }
            },
            modifier = Modifier.width(120.dp).height(36.dp).padding(start = 4.dp, top = (2.5).dp),
            colors = buttonColors,
            elevation = null,
            border = null,
        ) {
            Text(
                text = "Open Proj.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }

        Button(
            onClick = {
                val filenameSet = openFileDialog(null, "Select File", listOf("json"), false)
                if (filenameSet.isNotEmpty()) {
                    SaveManager.save(filenameSet.first().path)
                }
            },
            modifier = Modifier.width(120.dp).height(36.dp).padding(start = 4.dp, top = (2.5).dp),
            colors = buttonColors,
            elevation = null,
            border = null,
        ) {
            Text(
                text = "Save Proj.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}
