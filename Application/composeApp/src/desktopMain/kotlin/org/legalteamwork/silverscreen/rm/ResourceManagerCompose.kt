package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.*
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.ResourceManagerTheme
import org.legalteamwork.silverscreen.rm.window.effects.EffectsMainWindow
import org.legalteamwork.silverscreen.rm.window.source.SourcesMainWindow
import org.legalteamwork.silverscreen.windows.block.column
import java.awt.datatransfer.DataFlavor

@Composable
fun AppScope.ResourceManagerCompose() {
    BoxWithConstraints(
        modifier = Modifier.background(
            color = ResourceManagerTheme.RESOURCE_MANAGER_BACKGROUND_COLOR,
            shape = RoundedCornerShape(8.dp),
        ).fillMaxSize()
    ) {
        Row{
            ResourceManagerMenu()
            MainWindow(Modifier.weight(1f))
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
            .background(color = ResourceManagerTheme.MENU_COLOR, RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 0.dp,
                bottomStart = 8.dp,
                bottomEnd = 0.dp
            ))
            .fillMaxHeight()
            .width(110.dp)
    ){
        ResourceButtonList()
    }
}

/**
 * Список кнопок с иконками.
 */
@Composable
private fun AppScope.ResourceButtonList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            modifier = Modifier.padding(vertical = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResourceManager.resourceButtons.forEach { button ->
                ResourceIconButton(button)
            }
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
    val buttonColor = ResourceManagerTheme.MENU_BUTTONS_BACKGROUND_COLOR
    val clickedContentColor = ResourceManagerTheme.MENU_BUTTONS_CLICKED_CONTENT_COLOR
    val defaultContentColor = ResourceManagerTheme.MENU_BUTTONS_CONTENT_COLOR

    val isActive = currentType == button.resourceType

    Button(
        onClick = {
            ResourceManager.setResourceType(button.resourceType)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
            contentColor = if (isActive) clickedContentColor else defaultContentColor
        ),
        modifier = Modifier
            .size(80.dp)
            .border(0.dp, Color.Transparent),
        elevation = ButtonDefaults.elevation(0.dp),
        interactionSource = remember { MutableInteractionSource() },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(button.iconPath),
                contentDescription = button.resourceType.name,
                modifier = Modifier.size(40.dp),
                colorFilter = if (isActive) ColorFilter.tint(clickedContentColor) else null
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = button.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if (isActive) clickedContentColor else defaultContentColor
            )
        }
    }
}





/**
 * Отображенрие основного окна, которое содержит превью ресурсов,
 * с которыми можно взаимодействовать
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
private fun AppScope.MainWindow(modifier: Modifier) {
    val id by remember { ResourceManager.currentResourceType }

    Box(modifier = Modifier.fillMaxHeight().dragAndDropTarget(shouldStartDragAndDrop = { event ->
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
