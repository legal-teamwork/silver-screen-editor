package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.rm.window.*

/**
 * Базовый класс для файлового менеджера, реализующий смену окон (нажатия на вкладки) и содержащий методы отрисовки всего окна.
 * Синглтон, потому что вкладка единственна
 */
object ResourceManager {
    // Constants
    private const val INIT_ID = 1
    private const val SOURCES_ID = 1
    private const val EFFECTS_ID = 2
    private const val PRESETS_ID = 3
    private const val TEMPLATES_ID = 4
    private val MENU_MIN_WIDTH = 150.dp
    private val MENU_MAX_WIDTH = 275.dp
    private val MENU_BUTTON_HEIGHT = 35.dp
    private val MENU_FONT_FAMILY = FontFamily.Default

    // Fields:
    private val buttonId = mutableStateOf(INIT_ID)
    private val buttons = listOf(
        MenuButton(SOURCES_ID, "Sources"),
        MenuButton(EFFECTS_ID, "Effects"),
        MenuButton(PRESETS_ID, "Presets"),
        MenuButton(TEMPLATES_ID, "Templates"),
    )

    @Composable
    fun compose() {
        BoxWithConstraints(
            modifier = Modifier.background(
                color = Color(0xFF444444),
                shape = RoundedCornerShape(8.dp),
            ).fillMaxSize()
        ) {
            val adaptiveMenuWidth = max(min(maxWidth * 0.3f, MENU_MAX_WIDTH), MENU_MIN_WIDTH)
            val adaptiveMainWindowWidth = maxWidth - adaptiveMenuWidth

            Row {
                Menu(adaptiveMenuWidth)
                MainWindow(adaptiveMainWindowWidth)
            }
        }
    }

    /**
     * Отображение бокового меню
     */
    @Composable
    private fun Menu(menuWidth: Dp) {
        Box(
            modifier = Modifier
                .background(color = Color(0xFF3A3A3A), RoundedCornerShape(8.dp))
                .width(menuWidth)
                .fillMaxHeight()
        ) {
            ButtonList()
        }
    }

    /**
     * Отображение конкретно кнопок
     */
    @Composable
    private fun ButtonList() {
        Column(modifier = Modifier.padding(5.dp)) {
            for (button in buttons) {
                MenuButton(button)
            }
        }
    }

    /**
     * Отображение конкретной кнопки
     *
     * @param[button] инфомация о кнопке
     */
    @Composable
    private fun MenuButton(button: MenuButton) {
        var chosenButton by remember { buttonId }
        val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF3A3A3A),
            contentColor = Color.White,
            disabledBackgroundColor = Color(0xFF222222),
            disabledContentColor = Color.White,
        )

        Button(
            onClick = { chosenButton = button.id },
            modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
            colors = buttonColors,
            elevation = null,
            border = null,
            enabled = chosenButton != button.id,
        ) {
            Text(
                text = button.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                fontFamily = MENU_FONT_FAMILY,
            )
        }
    }

    /**
     * Отображенрие основного окна, которое содержит превью ресурсов,
     * с которыми можно взаимодействовать
     */
    @Composable
    private fun MainWindow(windowWidth: Dp) {
        val id by remember { buttonId }

        Box(modifier = Modifier.width(windowWidth).fillMaxHeight()) {
            when (id) {
                SOURCES_ID -> SourcesMenuButton()
                EFFECTS_ID -> EffectsMenuButton()
                PRESETS_ID -> PresetsMenuButton()
                TEMPLATES_ID -> TemplatesMenuButton()
                else -> ErrorMenuButton()
            }
        }
    }
}
