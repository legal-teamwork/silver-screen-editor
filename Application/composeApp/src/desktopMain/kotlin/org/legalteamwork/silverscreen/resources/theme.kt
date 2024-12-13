package org.legalteamwork.silverscreen.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

object AppTheme {
    val SURFACE_COLOR = Color.Black

    // Влияет вообще на все горизонтальные разделители(если есть желание, то можно будет разделить на отдельные разделители)
    val HORIZONTAL_DIVIDER_COLOR = Color.Black

    // Влияет вообще на все вертикальные разделители
    val VERTICAL_DIVIDER_COLOR = Color.Black
    val VIDEO_PANEL_BACKGROUND_COLOR = Color(0xFF232528)
    val VIDEO_EDITOR_BACKGROUND_COLOR = Color(0xFF232528)
}

object ResourceManagerTheme {
    val MENU_FONT_FAMILY = FontFamily.Default
    val RESOURCE_MANAGER_BACKGROUND_COLOR = Color(0xFF232528)
    //val MENU_COLOR = Color(0xFF3A3A3A)
    val MENU_COLOR = Color(0xFF232528)
    //val MENU_BUTTONS_BACKGROUND_COLOR = Color(0xFF3A3A3A)
    val MENU_BUTTONS_BACKGROUND_COLOR = Color(0xFF232528)
    val MENU_BUTTONS_CONTENT_COLOR = Color.White
    //val MENU_BUTTONS_DISABLED_BACKGROUND_COLOR = Color(0xFF222222)
    val MENU_BUTTONS_DISABLED_BACKGROUND_COLOR = Color(0xFF232528)
    val MENU_BUTTONS_DISABLED_CONTENT_COLOR = Color.White
}

object EditingPanelTheme {
    val DROPPABLE_FILE_BACKGROUND_COLOR_1 = Color(0xDDEAF6FF)
    val DROPPABLE_FILE_BACKGROUND_COLOR_2 = Color(0xDDD3F0FF)
    val DROPPABLE_FILE_BACKGROUND_COLOR_3 = Color(0xDDEFFDFF)
    val DROPPABLE_FILE_BACKGROUND_COLOR_4 = Color(0xDDCDF4E9)
    val DROPPABLE_FILE_BACKGROUND_COLOR_5 = Color(0xDDD0E8FF)


    val DROPPABLE_FILE_TEXT_COLOR = Color.Black
    val SHORT_MARK_INTERVAL_COLOR = Color.Gray
    val LONG_MARK_INTERVAL_COLOR = Color.Black
    val HIGHLIGHTED_DROPPABLE_FILE_BACKGROUND_COLOR = Color(0xFFF54C4C)

    // Здесь будет цвет той сущности
    val VIDEO_TRACK_BACKGROUND_COLOR = Color(0x337D8791)
    val AUDIO_TRACK_BACKGROUND_COLOR = Color(0x337D8791)

    val TRACK_INFO_BACKGROUND_COLOR = Color(0xFF222528)
    val TRACK_INFO_TEXT_COLOR = Color(0xFFE7E9EB)

    val EDITING_PANEL_BACKGROUND = Color.Black
    val TOOL_PANEL_COLOR = Color(0xFF232528)
    val TOOL_BUTTONS_BACKGROUND_COLOR = Color(0xFF232528)
    val TOOL_BUTTONS_CONTENT_COLOR = Color.White
    val TOOL_BUTTONS_DISABLED_BACKGROUND_COLOR = Color(0xFF222222)
    val TOOL_BUTTONS_DISABLED_CONTENT_COLOR = Color.White
    val TRACKS_PANEL_BACKGROUND_COLOR = Color(0xFF232528)
    val SLIDER_COLOR = Color.White
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
    val PATH_WINDOW_TEXT_COLOR = Color.White
    val NAV_WINDOW_BACKGROUND_COLOR = Color(0xFF232528)
    val BUTTON_OUTLINE_COLOR = Color(0xFF232528)
    val LIST_VIEW_RESOURCES_OUTLINE_COLOR = Color(0xFF232528)
    val LIST_VIEW_ADDITIONAL_INFO_COLOR = Color(0xFF232528)
    val LIST_ADD_BUTTON_OUTLINE_COLOR = Color(0xFF232528)
    val LIST_VIEW_ADD_BUTTON_TEXT_COLOR = Color.White
}

object SourcePreviewItemTheme {
    val ACTIVE_RESOURCE_OUTLINE_COLOR = Color.Blue
    val RESOURCE_NAME_OUTLINE_COLOR = Color.Black
    val RESOURCE_TEXT_COLOR = Color.White
    val RESOURCE_CURSOR_BRUSH = Color.Magenta
}

object ResourcePropertiesContextWindowTheme {
    val BORDER_COLOR = Color(0x44FFFFFF)
    val PROPERTY_NAME_COLOR = Color.LightGray
    val EACH_PROPERTY_TEXT_COLOR = Color.White
}

object ResourceContextWindowPatternTheme {
    val BACKGROUND_COLOR = Color(0xFF232528)
}

object ResourceActionsContextWindowTheme {
    val DIVIDERS_COLOR = Color.LightGray
    val TEXT_COLOR = Color.White
}

object NewFolderWindowTheme {
    val BACKGROUND_COLOR = Color(0xFF232528)
    val BORDER_COLOR = Color.LightGray
    val TEXT_COLOR = Color.White
    val CURSOR_BRUSH_COLOR = Color.Blue
    val INNER_BORDER_COLOR = Color.LightGray
    val INNER_BACKGROUND_COLOR = Color.Black
}

object MoveToWindowTheme {
    val BACKGROUND_COLOR = Color(0xFF222222)
    val BORDER_COLOR = Color.LightGray
    val TEXT_COLOR = Color.White
}

object CommandWindowTheme {
    val TEXT_COLOR = Color.White

    val COMMANDS_ICON_MAJOR_COLOR = Color.Green
    val COMMANDS_ICON_MINOR_COLOR = Color.Red
}
