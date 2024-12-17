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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.edit.CutResourceOnTrackCommand
import org.legalteamwork.silverscreen.command.edit.DeleteResourcesOnTrackCommand
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import org.legalteamwork.silverscreen.vp.VideoPanel
import kotlin.math.max

// Количество Dp в кадре.
@Suppress("ktlint:standard:property-naming")
var DpInFrame by mutableStateOf(1f)

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppScope.EditingPanel(panelHeight: Dp) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.5.dp),
        modifier =
            Modifier
                .background(
                    color = EditingPanelTheme.EDITING_PANEL_BACKGROUND,
                ),
    ) {
        InstrumentsPanel()
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
        val distance = Dimens.FRAME_RATE * DpInFrame * 5.dp
        val minDistance = Dimens.FRAME_RATE * 0.75f * 5.dp
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
                .height(panelHeight / 3),
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
                    val currentTimestamp = (tapOffset.x * 1000 / (Dimens.FRAME_RATE * DpInFrame)).toLong()
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

/**
 * Панель с инструментами.
 */
@Composable
private fun AppScope.InstrumentsPanel() {
    BoxWithConstraints(
        modifier =
            Modifier
                .background(
                    color = EditingPanelTheme.TOOL_PANEL_COLOR,
                    shape = RoundedCornerShape(8.dp),
                )
                .fillMaxHeight()
                .width(80.dp)
                .padding(3.5.dp),
    ) {
        val buttonColors =
            ButtonDefaults.buttonColors(
                backgroundColor = EditingPanelTheme.TOOL_BUTTONS_BACKGROUND_COLOR,
                contentColor = EditingPanelTheme.TOOL_BUTTONS_CONTENT_COLOR,
                disabledBackgroundColor = EditingPanelTheme.TOOL_BUTTONS_DISABLED_BACKGROUND_COLOR,
                disabledContentColor = EditingPanelTheme.TOOL_BUTTONS_DISABLED_CONTENT_COLOR,
            )

        Column {
            Button(
                modifier =
                    Modifier
                        .width(80.dp)
                        .height(50.dp)
                        .padding(0.dp),
                onClick = {
                    DpInFrame += 0.25f
                    if (DpInFrame > 2.5f) {
                        DpInFrame = 2.5f
                    }
                    VideoTrack.updateResourcesOnTrack()
                },
                colors = buttonColors,
            ) {
                Text(
                    text = String.format("+"),
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            Button(
                modifier =
                    Modifier
                        .width(80.dp)
                        .height(55.dp)
                        .padding(top = 5.dp),
                onClick = {
                    DpInFrame -= 0.25f
                    if (DpInFrame < 0.75f) {
                        DpInFrame = 0.75f
                    }
                    VideoTrack.updateResourcesOnTrack()
                },
                colors = buttonColors,
            ) {
                Text(
                    text = String.format("-"),
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            Button(
                modifier =
                    Modifier
                        .width(80.dp)
                        .height(55.dp)
                        .padding(top = 5.dp),
                onClick = {
                    if (VideoPanel.playbackManager.isPlaying.value)
                        VideoPanel.playbackManager.pause()

                    val position = Slider.getPosition()
                    val index = VideoTrack.resourcesOnTrack.indexOfFirst{ it.isPosInside(position) }
                    if (index != -1) {
                        commandManager.execute(CutResourceOnTrackCommand(VideoTrack, position, index))
                    }
                },
                colors = buttonColors,
            ) {
                Text(
                    text = String.format("cut"),
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }

            Button(
                modifier =
                    Modifier
                        .width(80.dp)
                        .height(55.dp)
                        .padding(top = 5.dp),
                onClick = {
                    val highlightedResources = VideoEditor.getHighlightedResources()
                    if (highlightedResources.size > 0) {
                        commandManager.execute(DeleteResourcesOnTrackCommand(VideoTrack, highlightedResources))
                    }
                },
                colors = buttonColors,
            ) {
                Text(
                    text = String.format("del"),
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
