package org.legalteamwork.silverscreen.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

val MAIN_COLOR_1 = Color(0xFF0A090C)
val MAIN_COLOR_2 = Color(0xFF161216)
//val MAIN_COLOR_3 = Color(0xFFFFD369)
val MAIN_COLOR_3 = Color(0xCCE08E45)
val MAIN_COLOR_4 = Color(0xFFF0EDEE)
val MAIN_COLOR_5 = Color(0xFFFF4545)
val MAIN_COLOR_5_1 = Color(0xFFFF8585)
val MAIN_COLOR_5_2 = Color(0xFFFFEBEB)
val MAIN_COLOR_5_3 = Color(0xFFFFADAD)
val MAIN_COLOR_6 = Color(0xAA0B090B)
val GRADIENT_1 = arrayOf(
    0f to MAIN_COLOR_5,
    1f to MAIN_COLOR_5_1
)
val GRADIENT_2 = arrayOf(
    0f to MAIN_COLOR_5_3,
    1f to MAIN_COLOR_5_2
)

object AppTheme {
    val SURFACE_COLOR = MAIN_COLOR_2

    // Влияет вообще на все горизонтальные разделители(если есть желание, то можно будет разделить на отдельные разделители)
    val HORIZONTAL_DIVIDER_COLOR = Color.Black

    // Влияет вообще на все вертикальные разделители
    val VERTICAL_DIVIDER_COLOR = Color.Black
    val VIDEO_PANEL_BACKGROUND_COLOR = MAIN_COLOR_2
    val VIDEO_EDITOR_BACKGROUND_COLOR = MAIN_COLOR_1
    val RESOURCE_MANAGER_BACKGROUND_COLOR = MAIN_COLOR_1
}

object ResourceManagerTheme {
    val MENU_FONT_FAMILY = FontFamily.Default
    val MENU_COLOR = GRADIENT_1
    val MENU_BUTTONS_BACKGROUND_COLOR = Color(0xFF191825)
    val MENU_BUTTONS_CONTENT_COLOR = MAIN_COLOR_4
    val MENU_BUTTONS_DISABLED_BACKGROUND_COLOR = Color(0xFF191825)
    val MENU_BUTTONS_DISABLED_CONTENT_COLOR = MAIN_COLOR_4
}

object EditingPanelTheme {
    val DROPPABLE_FILE_BACKGROUND_COLOR = MAIN_COLOR_4
    val DROPPABLE_FILE_TEXT_COLOR = MAIN_COLOR_4

    val RESOURCE_RESHAPE_AREA_COLOR = Color.Gray

    // Здесь будет цвет той сущности
    val VIDEO_TRACK_BACKGROUND_COLOR = Color(0xFF222528)
    val AUDIO_TRACK_BACKGROUND_COLOR = Color(0xFF222528)

    val TRACK_INFO_BACKGROUND_COLOR = Color(0xFF222528)
    val TRACK_INFO_TEXT_COLOR = Color(0xFFE7E9EB)

    val EDITING_PANEL_BACKGROUND = MAIN_COLOR_2
    val TOOL_PANEL_COLOR = MAIN_COLOR_1
    val TOOL_BUTTONS_BACKGROUND_COLOR = Color(0xFF191825)
    val TOOL_BUTTONS_CONTENT_COLOR = MAIN_COLOR_4
    val TOOL_BUTTONS_DISABLED_BACKGROUND_COLOR = Color(0xFF222222)
    val TOOL_BUTTONS_DISABLED_CONTENT_COLOR = MAIN_COLOR_4
    val TRACKS_PANEL_BACKGROUND_COLOR = MAIN_COLOR_1
    val SLIDER_COLOR = MAIN_COLOR_4
}

object MainWindowTheme {
    val EFFECTS_MAIN_WINDOW_TEXT_COLOR = Color.White
    val ERROR_MAIN_WINDOW_TEXT_COLOR = Color.Red
    val PRESETS_MAIN_WINDOW_TEXT_COLOR = Color.White
    val TEMPLATES_MAIN_WINDOW_TEXT_COLOR = Color.White
}

object SourcesMenuButtonTheme {
    val DIVIDER_COLOR = Color.Black
    val PATH_WINDOW_COLOR = Color(0xFF232528)
    val PATH_WINDOW_TEXT_COLOR = MAIN_COLOR_4
    val NAV_WINDOW_BACKGROUND_COLOR = Color(0xFF232528)
    val BUTTON_OUTLINE_COLOR = Color(0xFF232528)
    val LIST_VIEW_RESOURCES_OUTLINE_COLOR = Color(0xFF232528)
    val LIST_VIEW_ADDITIONAL_INFO_COLOR = Color(0xFF232528)
    val LIST_ADD_BUTTON_OUTLINE_COLOR = Color(0xFF232528)
    val LIST_VIEW_ADD_BUTTON_TEXT_COLOR = MAIN_COLOR_4
}

object SourcePreviewItemTheme {
    val ACTIVE_RESOURCE_OUTLINE_COLOR = Color.Blue
    val RESOURCE_NAME_OUTLINE_COLOR = Color.Black
    val RESOURCE_TEXT_COLOR = MAIN_COLOR_4
    val RESOURCE_CURSOR_BRUSH = Color.Magenta
}

object ResourcePropertiesContextWindowTheme {
    val BORDER_COLOR = Color(0x44FFFFFF)
    val PROPERTY_NAME_COLOR = Color.LightGray
    val EACH_PROPERTY_TEXT_COLOR = MAIN_COLOR_4
}

object ResourceContextWindowPatternTheme {
    val BACKGROUND_COLOR = Color(0xFF232528)
}

object ResourceActionsContextWindowTheme {
    val DIVIDERS_COLOR = Color.LightGray
    val TEXT_COLOR = MAIN_COLOR_4
}

object NewFolderWindowTheme {
    val BACKGROUND_COLOR = Color(0xFF232528)
    val BORDER_COLOR = Color.LightGray
    val TEXT_COLOR = MAIN_COLOR_4
    val CURSOR_BRUSH_COLOR = Color.Blue
    val INNER_BORDER_COLOR = Color.LightGray
    val INNER_BACKGROUND_COLOR = Color.Black
}

object MoveToWindowTheme {
    val BACKGROUND_COLOR = Color(0xFF222222)
    val BORDER_COLOR = Color.LightGray
    val TEXT_COLOR = MAIN_COLOR_4
}

object CommandWindowTheme {
    val TEXT_COLOR = MAIN_COLOR_4

    val COMMANDS_ICON_MAJOR_COLOR = MAIN_COLOR_5
    val COMMANDS_ICON_MINOR_COLOR = Color.Red
}
