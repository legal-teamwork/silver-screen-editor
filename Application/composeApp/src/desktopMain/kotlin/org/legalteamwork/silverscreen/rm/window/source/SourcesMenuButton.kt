package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.Density
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.decodeToSvgPainter
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.SourcesMenuButtonTheme
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.ResourceManager.isListView
import org.legalteamwork.silverscreen.rm.ResourceManager.toggleViewMode
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.*
import silverscreeneditor.composeapp.generated.resources.Res
import silverscreeneditor.composeapp.generated.resources.add_folder
import silverscreeneditor.composeapp.generated.resources.up
import java.io.File

private val logger = KotlinLogging.logger {  }

@Composable
fun AppScope.SourcesMainWindow() {
    var contextWindow by remember { mutableStateOf<ContextWindow?>(null) }
    val onContextWindowOpen: (ContextWindow?) -> Unit = { contextWindow = it }
    val onContextWindowClose: () -> Unit = { contextWindow = null }

    BoxWithConstraints {
        Column {
            NavWindow(onContextWindowOpen, onContextWindowClose)
            Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = SourcesMenuButtonTheme.DIVIDER_COLOR)
            PathWindow()
            Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = SourcesMenuButtonTheme.DIVIDER_COLOR)

            Box(modifier = Modifier.fillMaxSize()) {
                if (isListView.value) {
                    // Режим списка
                    LazyColumn {
                        items(
                            items = ResourceManager.currentFolder.value.resources.toList(),
                            key = Resource::hashCode
                        ) { resource ->
                            ListViewItem(resource)
                        }

                        item {
                            ListAddButton()
                        }
                    }
                } else {
                    // Режим сетки (существующий код)
                    SourcesPreviews(onContextWindowOpen, onContextWindowClose)
                }
            }
        }

        ContextWindow(contextWindow, onContextWindowOpen, onContextWindowClose)
    }
}

@Composable
fun PathWindow() {
    Box(
        Modifier.fillMaxWidth()
            .height(Dimens.NAV_MENU_HEIGHT)
            .background(SourcesMenuButtonTheme.PATH_WINDOW_COLOR),
        contentAlignment = Alignment.CenterStart) {
        val pathText = ResourceManager.getRelativePath(ResourceManager.currentFolder.value)

        Text(
            text = pathText,
            color = SourcesMenuButtonTheme.PATH_WINDOW_TEXT_COLOR,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun NavWindow(
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    Box(Modifier.fillMaxWidth().height(Dimens.NAV_MENU_HEIGHT).background(SourcesMenuButtonTheme.NAV_WINDOW_BACKGROUND_COLOR)) {
        Row(
            modifier = Modifier.fillMaxSize().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .border(1.dp, SourcesMenuButtonTheme.BUTTON_OUTLINE_COLOR, RoundedCornerShape(5.dp))
                    .clickable {
                        ResourceManager.onFolderUp()
                    }
            ) {
                Image(
                    painter = painterResource(Res.drawable.up),
                    contentDescription = "Up",
                    modifier = Modifier.size(Dimens.NAV_ICON_SIZE).padding(5.dp)
                )
            }

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .border(1.dp, SourcesMenuButtonTheme.BUTTON_OUTLINE_COLOR, RoundedCornerShape(5.dp))
                    .clickable {
                        val contextWindowId = ContextWindowId.NEW_FOLDER
                        val contextWindowData = ContextWindowData(Offset.Zero)
                        val contextWindow = ContextWindow(contextWindowId, contextWindowData)
                        onContextWindowOpen(contextWindow)
                    }
            ) {
                Image(
                    painter = painterResource(Res.drawable.add_folder),
                    contentDescription = "Add folder",
                    modifier = Modifier.size(Dimens.NAV_ICON_SIZE).padding(5.dp)
                )
            }

            // Кнопка переключения режима
            Button(
                onClick = {
                    logger.info { "View mode button clicked" }
                    toggleViewMode()
                },
            ) {
                Text(if (isListView.value) "Switch to Icons" else "Switch to List")
            }
        }
    }
}

@Composable
private fun AppScope.SourcesPreviews(
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = Dimens.COLUMN_MIN_WIDTH)) {
            items(
                items = ResourceManager.currentFolder.value.resources.toList(),
                key = Resource::hashCode
            ) { resource ->
                SourcePreviewItem(
                    resource = resource, onContextWindowOpen, onContextWindowClose
                )
            }

            item {
                SourceAddButton()
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ListViewItem(resource: Resource) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(0.5.dp, SourcesMenuButtonTheme.LIST_VIEW_RESOURCES_OUTLINE_COLOR)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Миниатюра
        logger.info { "Adding miniature of file" }
        if (File(resource.previewPath).extension == "svg") {
            Image(
                painter = File(resource.previewPath).inputStream().readAllBytes().decodeToSvgPainter(Density(1f)),
                contentDescription = resource.title.value,
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Image(
                painter = BitmapPainter(remember {
                    File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
                }),
                contentDescription = resource.title.value,
                modifier = Modifier
                    .size(40.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Название файла
        Text(
            text = resource.title.value,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            color = SourcesMenuButtonTheme.PATH_WINDOW_TEXT_COLOR,
        )

        // Можно добавить дополнительную информацию
        Text(
            text = "Size: ${File(resource.previewPath).length() / 1024} KB",
            color = SourcesMenuButtonTheme.LIST_VIEW_ADDITIONAL_INFO_COLOR
        )
    }
}

@Composable
private fun AppScope.ListAddButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(0.5.dp, SourcesMenuButtonTheme.LIST_ADD_BUTTON_OUTLINE_COLOR)
            .padding(8.dp)
            .clickable(
                onClickLabel = "Add resource",
                role = Role.Button,
                onClick = { ResourceManager.addSourceTriggerActivity(commandManager) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource("svg/add-resource.svg"),
            contentDescription = "Add resource button",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Add New Resource",
            color = SourcesMenuButtonTheme.LIST_VIEW_ADD_BUTTON_TEXT_COLOR,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun AppScope.ContextWindow(
    contextWindow: ContextWindow?,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    contextWindow?.apply {
        logger.info { "Context menu opened" }
        when (id) {
            ContextWindowId.CONTEXT_MENU -> ResourceActionsContextWindow(
                data,
                onContextWindowOpen,
                onContextWindowClose
            )

            ContextWindowId.PROPERTIES -> ResourcePropertiesContextWindow(
                data,
                onContextWindowOpen,
                onContextWindowClose
            )

            ContextWindowId.MOVE_TO -> MoveToWindow(data, onContextWindowOpen, onContextWindowClose)
            ContextWindowId.COPY_TO -> CopyToWindow(data, onContextWindowOpen, onContextWindowClose)
            ContextWindowId.NEW_FOLDER -> NewFolderWindow(data, onContextWindowOpen, onContextWindowClose)
        }
    }
}
