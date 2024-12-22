package org.legalteamwork.silverscreen.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

object AppTheme {
    val SURFACE_COLOR = Color.Black

    // Влияет вообще на все горизонтальные разделители(если есть желание, то можно будет разделить на отдельные разделители)
    val HORIZONTAL_DIVIDER_COLOR = Color.Black

    // Влияет вообще на все вертикальные разделители
    val VERTICAL_DIVIDER_COLOR = Color.Black
    val VIDEO_PANEL_BACKGROUND_COLOR = Color(0xFF21282D)
    val VIDEO_PANEL_VIDEO_COLOR = Color(0xFF21282D)

    val COMMAND_WINDOW_BACKGROUND = Color(0xFF21282D)
    val VIDEO_EDITOR_BACKGROUND_COLOR = Color(0xFF232528)

    val RENDER_BACKGROUND_COLOR = Color(0xFF1A1A1A)
    val RENDER_BORDER_COLOR = Color(0xFFAAAAAA)
    val RENDER_ACTIVE_COLOR = Color(0xFF55E5C5)
    val RENDER_BAR_MAIN_COLOR = Color(0xFF333333)
    val RENDER_TEXT_COLOR = Color(0xFFFFFFFF)
}

object ResourceManagerTheme {
    val MENU_FONT_FAMILY = FontFamily.Default
    val RESOURCE_MANAGER_BACKGROUND_COLOR = Color(0xFF0D1014)

    val MENU_COLOR = Color(0xFF21282D)

    val MENU_BUTTONS_BACKGROUND_COLOR = Color(0xFF21282D)
    val MENU_BUTTONS_CONTENT_COLOR = Color(0xFFFFFFFF)
    val MENU_BUTTONS_CLICKED_CONTENT_COLOR = Color(0xFF55E5C5)
    //val MENU_BUTTONS_CLICKED_CONTENT_COLOR = Color.Red
}

object EditingPanelTheme {
    val RESOURCE_COLOR_DEFAULT = Color(0xFF1A1A1A)
    //val RESOURCE_COLOR_POINTED = Color.Red
    val RESOURCE_COLOR_CLICKED = Color(0xFF55E5C5)

    val EFFECT_BACKGROUND_COLOR = Color(0xFF55E5C5)

    val DROPPABLE_FILE_TEXT_COLOR_DEFAULT = Color(0xFFFFFFFF)
    val DROPPABLE_FILE_TEXT_COLOR_CLICKED = Color(0xFF000000)

    val SHORT_MARK_INTERVAL_COLOR = Color.Gray
    val LONG_MARK_INTERVAL_COLOR = Color.Black
    val HIGHLIGHTED_DROPPABLE_FILE_BACKGROUND_COLOR = Color(0x337D8791)

    // Здесь будет цвет той сущности
    val VIDEO_TRACK_BACKGROUND_COLOR = Color(0xFF222A2E)
    val AUDIO_TRACK_BACKGROUND_COLOR = Color(0xFF222A2E)

    val TRACK_INFO_BACKGROUND_COLOR = Color(0xFF222528)
    val TRACK_INFO_TEXT_COLOR = Color(0xFFE7E9EB)

    val EDITING_PANEL_BACKGROUND = Color(0xFF222A2E)
    //val TOOL_PANEL_COLOR = Color(0xFF232528)
    val TOOLBOX_PANEL_BACKGROUND = Color(0xFF0D1014)
    val TOOL_BUTTONS_BACKGROUND_COLOR = Color(0xFF1A1A1A)
    val TOOL_BUTTONS_CONTENT_COLOR = Color(0xFFDFDFDF)
    val TOOL_BUTTONS_DISABLED_BACKGROUND_COLOR = Color(0xFF222222)
    val TOOL_BUTTONS_DISABLED_CONTENT_COLOR = Color.White
    val TRACKS_PANEL_BACKGROUND_COLOR = Color(0xFF1E2529)
    val SLIDER_COLOR = Color(0xFFFF6600)

    val ZOOM_SLIDER_COLOR = Color(0xFF666666)
    val ZOOM_SLIDER_COLOR_THUMB = Color(0xFF000000)
    val ZOOM_SLIDER_COLOR_BORDER = Color(0xFF55E5C5)
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
    val NAV_WINDOW_BACKGROUND_COLOR = Color(0xFF1E2529)
    val BUTTON_OUTLINE_COLOR = Color(0xFF232528)
    val LIST_VIEW_RESOURCES_OUTLINE_COLOR = Color(0xFFFFFFFF)
    val LIST_VIEW_ADDITIONAL_INFO_COLOR = Color(0xFFFFFFFF)
    val LIST_ADD_BUTTON_OUTLINE_COLOR = Color(0xFFFFFFFF)
    val LIST_VIEW_ADD_BUTTON_TEXT_COLOR = Color(0xFFFFFFFF)
}

object SourcePreviewItemTheme {
    val ACTIVE_RESOURCE_OUTLINE_COLOR = Color(0xFF55E5C5)
    val RESOURCE_NAME_OUTLINE_COLOR = Color.Black
    val RESOURCE_TEXT_COLOR = Color(0xFFFFFFFF)
    val RESOURCE_CURSOR_BRUSH = Color(0xFF55E5C5)
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

    val COMMANDS_ICON_MAJOR_COLOR = Color(0xFF21282D)
    val COMMANDS_ICON_MINOR_COLOR = Color(0xFF55E5C5)
}

object EffectsTabTheme {
    val EFFECT_BACKGROUND = Color(0xFF1E2529)
}
