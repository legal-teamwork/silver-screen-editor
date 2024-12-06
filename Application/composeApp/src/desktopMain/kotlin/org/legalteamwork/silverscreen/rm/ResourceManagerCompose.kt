package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.ResourceManagerTheme
import org.legalteamwork.silverscreen.rm.window.EffectsMainWindow
import org.legalteamwork.silverscreen.rm.window.source.SourcesMainWindow


/**
 * Основной компонент менеджера ресурсов.
 */
@Composable
fun AppScope.ResourceManagerCompose() {
    BoxWithConstraints(
        modifier = Modifier
            .background(
                color = ResourceManagerTheme.RESOURCE_MANAGER_BACKGROUND_COLOR,
                shape = RoundedCornerShape(8.dp),
            )
            .fillMaxSize()
    ) {

        Row {
            ResourceManagerMenu()
            MainWindow()
        }
    }
}

/**
 * Компонент бокового меню с иконками.
 * @param menuWidth ширина меню.
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
 * Список кнопок ресурс-менеджера.
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
 * Отдельная кнопка ресурс-менеджера.
 * @param button информация о кнопке.
 */
@Composable
private fun AppScope.ResourceIconButton(button: ResourceManager.ResourceButton) {
    val currentType by remember { ResourceManager.currentResourceType }

    IconButton(
        onClick = { ResourceManager.setResourceType(button.resourceType) },
        modifier = Modifier.size(64.dp) // Увеличьте размер кнопки
    ) {
        Image(
            painter = painterResource(button.iconPath),
            contentDescription = button.resourceType.name,
            modifier = Modifier.size(48.dp) // Увеличьте размер иконки
        )
    }
}

/**
 * Отображенрие основного окна, которое содержит превью ресурсов,
 * с которыми можно взаимодействовать
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun AppScope.MainWindow() {
    val id by remember { ResourceManager.currentResourceType }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pointerInput(Unit) {
                // Обработка drag-and-drop
                detectDragGestures(onDrag = { change, dragAmount ->
                    // Обработка перетаскивания
                })
            }
    ) {
        when (id) {
            ResourceType.EFFECTS -> EffectsMainWindow()
            // ... другие окна
            else -> SourcesMainWindow()
        }
    }
}

/**
 * Настройки отображения главного окна.
 * @param showGrid показывать сетку.
 * @param gridSize размер сетки.
 */
data class MainWindowSettings(
    val showGrid: Boolean = true,
    val gridSize: Int = 64
)

