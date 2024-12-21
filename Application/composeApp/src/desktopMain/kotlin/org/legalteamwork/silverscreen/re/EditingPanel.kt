@file:OptIn(ExperimentalFoundationApi::class)

package org.legalteamwork.silverscreen.re

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.vp.VideoPanel
import kotlin.math.max

// Количество Dp в кадре.
@Suppress("ktlint:standard:property-naming")
var DpPerSecond by mutableStateOf(30f)
var DpInFrame: Float
    get() = (DpPerSecond / Project.fps).toFloat()
    set(value) {
        DpPerSecond = (value * Project.fps).toFloat()
    }

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppScope.EditingPanel(panelHeight: Dp, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.5.dp),
        modifier =
            Modifier
                .background(
                    color = EditingPanelTheme.EDITING_PANEL_BACKGROUND,
                ),
    ) {
        TimelinesPanel(panelHeight)
    }
}

/**
 * Панель с дорожками.
 */
@Composable
private fun AppScope.TimelinesPanel(panelHeight: Dp) {
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.value) {
        Slider.updateScrollOffset(scrollState.value)
    }

    BoxWithConstraints(
        modifier =
            Modifier
                .background(
                    color = EditingPanelTheme.TRACKS_PANEL_BACKGROUND_COLOR,
                    shape = RoundedCornerShape(8.dp),
                )
                .fillMaxSize()
                .clipToBounds(), // <-- Нужно чтобы слайдер не заезжал на панель инструментов
    ) {
        val maxWidthVideos = (VideoEditor.getResourcesOnTrack().maxOfOrNull { it.getRightBorder() })?.dp ?: 0.dp
        val totalMaximumWidth = maxOf(maxWidthVideos, this@BoxWithConstraints.maxWidth)
        val distance = DpPerSecond * 5.dp
        val minDistance = Project.fps * 0.75f * 5.dp
        val minTotalBlocks = (totalMaximumWidth / minDistance).toInt() + 1
        val totalBlocks = max((totalMaximumWidth / distance).toInt() + 1, minTotalBlocks)
        val timelineLength = totalBlocks * distance

        Box(modifier = Modifier.horizontalScroll(scrollState).fillMaxWidth()) {
            TimelineMarks(totalBlocks, distance)
            TimelineTracks(panelHeight, timelineLength)
        }

        Box {
            SliderCompose(panelHeight)
        }
    }
}

@Composable
private fun AppScope.TimelineTracks(
    panelHeight: Dp,
    timelineLength: Dp
) {
    Column(
        modifier =
            Modifier
                .padding(top = 55.dp)
                .height(115.dp),
        verticalArrangement = Arrangement.Center
    ) {
        VideoTrackCompose(timelineLength)
    }
}

@Composable
private fun TimelineMarks(totalBlocks: Int, distance: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val currentTimestamp = (tapOffset.x * 1000 / DpPerSecond).toLong()
                    Slider.updatePosition(currentTimestamp)
                    if (VideoPanel.playbackManager.isPlaying.value) {
                        VideoPanel.playbackManager.seekToExactPositionWhilePlaying(currentTimestamp)
                    } else {
                        VideoPanel.playbackManager.seekToExactPosition(currentTimestamp)
                    }
                }
            },
    ) {
        Row {
            for (i in 0 until totalBlocks) {
                Box(modifier = Modifier.width(distance).height(45.dp)) {
                    Column {
                        Box(modifier = Modifier.width(distance).height(25.dp)) {
                            Box(modifier = Modifier.width(2.dp).height(25.dp).background(Color.White))
                            if (i * 5 < 60) {
                                Text(
                                    text = String.format("%ds", i * 5),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 8.dp),
                                )
                            } else {
                                Text(
                                    text = String.format("%dm %ds", (i * 5) / 60, (i * 5) % 60),
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 8.dp),
                                )
                            }
                        }
                        Box(modifier = Modifier.width(distance).height(20.dp)) {
                            Row {
                                for (j in 1..5) {
                                    Row {
                                        Box(
                                            modifier = Modifier.width(2.dp).height(20.dp)
                                                .background(Color.White)
                                        )
                                        Box(
                                            modifier =
                                                Modifier.width(
                                                    (distance - 10.dp) / 5,
                                                ).height(20.dp)
                                                    .background(EditingPanelTheme.TRACKS_PANEL_BACKGROUND_COLOR),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
