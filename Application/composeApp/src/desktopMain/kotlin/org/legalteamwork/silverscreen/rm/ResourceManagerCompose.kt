package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.ResourceManagerTheme
import org.legalteamwork.silverscreen.rm.window.effects.EffectsMainWindow
import org.legalteamwork.silverscreen.rm.ResourceManager.tabId
import org.legalteamwork.silverscreen.rm.window.effects.EffectsMainWindow
import org.legalteamwork.silverscreen.rm.window.source.SourcesMainWindow
import java.awt.datatransfer.DataFlavor

@Composable
fun AppScope.ResourceManagerCompose() {
    BoxWithConstraints(
        modifier = Modifier.background(
            color = ResourceManagerTheme.RESOURCE_MANAGER_BACKGROUND_COLOR,
            shape = RoundedCornerShape(8.dp),
        ).fillMaxSize()
    ) {
        val adaptiveMenuWidth = max(min(maxWidth * 0.3f, Dimens.MENU_MAX_WIDTH), Dimens.MENU_MIN_WIDTH)
        val adaptiveMainWindowWidth = maxWidth - adaptiveMenuWidth

        Row {
            ResourceManagerMenu()
            MainWindow(adaptiveMainWindowWidth)
        }
    }
}

/**
 * Боковое меню с иконками.
 */
@Composable
private fun AppScope.ResourceManagerMenu() {
    Box(
        modifier = Modifier
            .background(color = ResourceManagerTheme.MENU_COLOR, shape = RoundedCornerShape(8.dp))
            .fillMaxHeight()
    ) {
        ResourceButtonList()
    }
}

/**
 * Список кнопок с иконками.
 */
@Composable
private fun AppScope.ResourceButtonList() {
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ResourceManager.resourceButtons.forEach { button ->
            ResourceIconButton(button)
        }
    }
}

/**
 * Отдельная кнопка с иконкой.
 * @param button информация о кнопке.
 */
@Composable
private fun AppScope.ResourceIconButton(button: ResourceManager.ResourceButton) {
    val currentType by remember { ResourceManager.currentResourceType }

    IconButton(
        onClick = { ResourceManager.setResourceType(button.resourceType) },
        modifier = Modifier.size(64.dp) // Размер кнопки
    ) {
        Image(
            painter = painterResource(button.iconPath),
            contentDescription = button.resourceType.name,
            modifier = Modifier.size(48.dp) // Размер иконки
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
    val id by remember { ResourceManager.currentResourceType }

    Box(modifier = Modifier.width(windowWidth).fillMaxHeight().dragAndDropTarget(shouldStartDragAndDrop = { event ->
        event.awtTransferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
    }, target = remember { MainWindowDragAndDropTarget(resourceManager, commandManager) })) {
        when (id) {
            ResourceType.EFFECTS -> EffectsMainWindow()
            //ResourceType.VIDEO -> SourcesMainWindow() // Пример для видео
            // Добавьте другие типы окон
            else -> SourcesMainWindow()
        }
    }
}
