package org.legalteamwork.silverscreen.resources

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Dimens {
    // App.kt
    const val RESOURCE_WINDOW_WEIGHT = 45f
    val RESOURCE_WINDOW_MIN_WIDTH = 400.dp
    val RESOURCE_WINDOW_MIN_HEIGHT = 400.dp
    val RESOURCE_WINDOW_MAX_WIDTH = 800.dp
    val RESOURCE_WINDOW_MAX_HEIGHT = Dp.Infinity

    const val PREVIEW_WINDOW_WEIGHT = 45f
    val PREVIEW_WINDOW_MIN_WIDTH = 400.dp
    val PREVIEW_WINDOW_MIN_HEIGHT = 400.dp
    val PREVIEW_WINDOW_MAX_WIDTH = Dp.Infinity
    val PREVIEW_WINDOW_MAX_HEIGHT = Dp.Infinity

    const val COMMANDS_WINDOW_WEIGHT = 10f
    val COMMANDS_WINDOW_MIN_WIDTH = 150.dp
    val COMMANDS_WINDOW_MIN_HEIGHT = 400.dp
    val COMMANDS_WINDOW_MAX_WIDTH = 300.dp
    val COMMANDS_WINDOW_MAX_HEIGHT = Dp.Infinity

    const val UPPER_WINDOW_WEIGHT = 70f
    const val TIMELINE_WINDOW_WEIGHT = 30f
    val TIMELINE_WINDOW_MIN_WIDTH = 0.dp
    val TIMELINE_WINDOW_MIN_HEIGHT = 200.dp
    val TIMELINE_WINDOW_MAX_WIDTH = Dp.Infinity
    val TIMELINE_WINDOW_MAX_HEIGHT = Dp.Infinity

    //In ResourceManager.kt
    const val INIT_ID = 1
    const val SOURCES_ID = 1
    const val EFFECTS_ID = 2
    const val PRESETS_ID = 3
    const val TEMPLATES_ID = 4
    val MENU_MIN_WIDTH = 150.dp
    val MENU_MAX_WIDTH = 275.dp
    val MENU_BUTTON_HEIGHT = 35.dp

    //In EditingPanel.kt
    const val FRAME_RATE = 25f
    val VIDEO_TRACK_MAX_HEIGHT = 500.dp
    val VIDEO_TRACK_MIN_WIDTH = 30.dp
    val AUDIO_TRACK_MAX_HEIGHT = 500.dp
    val AUDIO_TRACK_MIN_WIDTH = 30.dp
    val MIN_SIZE_OF_RESOURCE_ON_TRACK = 1
    val RESOURCES_HORIZONTAL_OFFSET_ON_TRACK = 304.dp

    //In SourceMenuButton.kt
    val IMAGE_WIDTH = 250.dp
    val IMAGE_HEIGHT = 140.dp
    val CELL_PADDING = 5.dp
    val COLUMN_MIN_WIDTH = IMAGE_WIDTH + CELL_PADDING * 2
    val NAV_ICON_SIZE = 40.dp
    val NAV_MENU_HEIGHT = 50.dp

    //In CopyToWindow/MoveToWindow/NewFolderWindow.kt
    val WINDOW_WIDTH = 250.dp
    val WINDOW_HEIGHT = 400.dp

    //In App.kt
    val MARGIN_SIZE = 6.dp
    val DIVIDER_SIZE = 8.dp
    val WINDOW_CORNER_RADIUS = 10.dp

    // In command/CommandWindowCompose.kt
    val COMMAND_WINDOW_MARGIN = 8.dp
    val HEADER_TEXT_SIZE = 24.sp
    val COMMANDS_ICON_SIZE = 5.dp
    val COMMANDS_ICON_START_OFFSET = 15.dp
    val COMMANDS_ICON_END_OFFSET = 15.dp
}