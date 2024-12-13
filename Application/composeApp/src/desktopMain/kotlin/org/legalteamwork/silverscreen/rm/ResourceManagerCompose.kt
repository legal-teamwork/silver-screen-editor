package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.AppTheme
import org.legalteamwork.silverscreen.resources.ResourceManagerTheme
import org.legalteamwork.silverscreen.rm.ResourceManager.tabId
import org.legalteamwork.silverscreen.rm.window.EffectsMainWindow
import org.legalteamwork.silverscreen.rm.window.ErrorMainWindow
import org.legalteamwork.silverscreen.rm.window.PresetsMainWindow
import org.legalteamwork.silverscreen.rm.window.TemplatesMainWindow
import org.legalteamwork.silverscreen.rm.window.source.SourcesMainWindow
import java.awt.datatransfer.DataFlavor
import androidx.compose.ui.graphics.Brush

@Composable
fun AppScope.ResourceManagerCompose() {
    BoxWithConstraints(
        modifier = Modifier.background(
            color = AppTheme.RESOURCE_MANAGER_BACKGROUND_COLOR,
            shape = RoundedCornerShape(8.dp),
        ).fillMaxSize()
    ) {
        val adaptiveMenuWidth = max(min(maxWidth * 0.3f, Dimens.MENU_MAX_WIDTH), Dimens.MENU_MIN_WIDTH)
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
private fun AppScope.Menu(menuWidth: Dp) {
    Box(
        modifier = Modifier.background(Brush.verticalGradient(colorStops = ResourceManagerTheme.MENU_COLOR), RoundedCornerShape(8.dp)).width(menuWidth)
            .fillMaxHeight()
    ) {
        ButtonList()
    }
}

/**
 * Отображение конкретно кнопок
 */
@Composable
private fun AppScope.ButtonList() {
    Column(modifier = Modifier.padding(5.dp)) {
        for (button in resourceManager.tabs) {
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
private fun AppScope.MenuButton(button: MenuButton) {
    var chosenButton by remember { tabId }
    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = ResourceManagerTheme.MENU_BUTTONS_BACKGROUND_COLOR,
        contentColor = ResourceManagerTheme.MENU_BUTTONS_CONTENT_COLOR,
        disabledBackgroundColor = ResourceManagerTheme.MENU_BUTTONS_DISABLED_BACKGROUND_COLOR,
        disabledContentColor = ResourceManagerTheme.MENU_BUTTONS_DISABLED_CONTENT_COLOR,
    )

    Button(
        onClick = { chosenButton = button.id },
        modifier = Modifier.fillMaxWidth().height(Dimens.MENU_BUTTON_HEIGHT),
        colors = buttonColors,
        elevation = null,
        border = null,
        enabled = chosenButton != button.id,
    ) {
        Text(
            text = button.title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left,
            fontFamily = ResourceManagerTheme.MENU_FONT_FAMILY,
        )
    }
}

/**
 * Отображенрие основного окна, которое содержит превью ресурсов,
 * с которыми можно взаимодействовать
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun AppScope.MainWindow(windowWidth: Dp) {
    val id by remember { resourceManager.tabId }

    Box(modifier = Modifier.width(windowWidth).fillMaxHeight().dragAndDropTarget(shouldStartDragAndDrop = { event ->
        event.awtTransferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
    }, target = remember { MainWindowDragAndDropTarget(resourceManager, commandManager) })) {
        when (id) {
            Dimens.SOURCES_ID -> SourcesMainWindow()
            Dimens.EFFECTS_ID -> EffectsMainWindow()
            Dimens.PRESETS_ID -> PresetsMainWindow()
            Dimens.TEMPLATES_ID -> TemplatesMainWindow()
            else -> ErrorMainWindow()
        }
    }
}
