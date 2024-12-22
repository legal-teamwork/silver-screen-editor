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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
            //PathWindow()
            //Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = SourcesMenuButtonTheme.DIVIDER_COLOR)

            Box(modifier = Modifier.fillMaxSize()) {
                if (isListView.value) {
                    // Режим списка
                    LazyColumn {
                        item {
                            ListAddButton()
                        }
                        items(
                            items = ResourceManager.currentFolder.value.resources.toList(),
                            key = Resource::hashCode
                        ) { resource ->
                            ListViewItem(resource)
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
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    //.border(1.dp, SourcesMenuButtonTheme.BUTTON_OUTLINE_COLOR, RoundedCornerShape(5.dp))
                    .clickable {
                        ResourceManager.onFolderUp()
                    }
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource("svg/up_folder_button.svg"),
                    contentDescription = "Up",
                    modifier = Modifier.size(Dimens.NAV_ICON_SIZE).padding(5.dp)
                )
            }

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    //.border(1.dp, SourcesMenuButtonTheme.BUTTON_OUTLINE_COLOR, RoundedCornerShape(5.dp))
                    .clickable {
                        val contextWindowId = ContextWindowId.NEW_FOLDER
                        val contextWindowData = ContextWindowData(Offset.Zero)
                        val contextWindow = ContextWindow(contextWindowId, contextWindowData)
                        onContextWindowOpen(contextWindow)
                    }
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource("svg/add_folder_button.svg"),
                    contentDescription = "Add folder",
                    modifier = Modifier.size(Dimens.NAV_ICON_SIZE).padding(5.dp),
                )
            }

            /*
            // Кнопка переключения режима
            Button(
                onClick = {
                    logger.info { "View mode button clicked" }
                    toggleViewMode()
                },
            ) {
                if (isListView.value){
                    Image(
                        painter = androidx.compose.ui.res.painterResource("svg/img_list_button.svg"),
                        contentDescription = "Add folder",
                        modifier = Modifier.size(Dimens.NAV_ICON_SIZE).padding(5.dp),
                    )
                }
                else{
                    Image(
                        painter = androidx.compose.ui.res.painterResource("svg/tbl_list_button.svg"),
                        contentDescription = "Add folder",
                        modifier = Modifier.size(Dimens.NAV_ICON_SIZE).padding(5.dp),
                    )
                }

                //Text(if (isListView.value) "Switch to Icons" else "Switch to List")
            }

             */

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    //.size(Dimens.NAV_ICON_SIZE)
                    //.border(1.dp, SourcesMenuButtonTheme.BUTTON_OUTLINE_COLOR, RoundedCornerShape(5.dp))
                    .clickable {
                        logger.info { "View mode button clicked" }
                        toggleViewMode()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (isListView.value) {
                        androidx.compose.ui.res.painterResource("svg/img_list_button.svg")
                    } else {
                        androidx.compose.ui.res.painterResource("svg/tbl_list_button.svg")
                    },
                    contentDescription = "Switch mode",
                    modifier = Modifier.size(Dimens.NAV_ICON_SIZE).padding(5.dp)
                )
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
        Spacer(modifier = Modifier.height(50.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = Dimens.COLUMN_MIN_WIDTH),
            modifier = Modifier.padding(top=10.dp).fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            item {
                SourceAddButton()
            }

            items(
                items = ResourceManager.currentFolder.value.resources.toList(),
                key = Resource::hashCode
            ) { resource ->
                SourcePreviewItem(
                    resource = resource, onContextWindowOpen, onContextWindowClose
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ListViewItem(resource: Resource) {
    Spacer(modifier = Modifier.padding(start = 5.dp, end = 5.dp).height(1.dp).fillMaxWidth().background(color = Color(0xFFFFFFFF)))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            //.border(0.5.dp, SourcesMenuButtonTheme.LIST_VIEW_RESOURCES_OUTLINE_COLOR)
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
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color(0xCCFFFFFF))
            )
        } else {
            Image(
                painter = BitmapPainter(remember {
                    File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
                }),
                contentDescription = resource.title.value,
                modifier = Modifier
                    .size(70.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit,
                //colorFilter = ColorFilter.tint(Color(0xCCFFFFFF))
            )
        }

        val rememberedTitle by remember { resource.title }
        // Название файла
        Text(
            text = rememberedTitle,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            color = Color(0xFFFFFFFF),
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
            //.border(0.5.dp, SourcesMenuButtonTheme.LIST_ADD_BUTTON_OUTLINE_COLOR)
            .padding(start = 25.dp)
            .clickable(
                onClickLabel = "Add resource",
                role = Role.Button,
                onClick = { ResourceManager.addSourceTriggerActivity(commandManager) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Image(
            painter = androidx.compose.ui.res.painterResource("svg/plus_big_button.svg"),
            contentDescription = "Add resource button",
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Add New Resource",
            color = SourcesMenuButtonTheme.LIST_VIEW_ADD_BUTTON_TEXT_COLOR,
            modifier = Modifier.padding(start = 25.dp)
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
            ContextWindowId.NEW_FOLDER -> NewFolderWindow(data, onContextWindowOpen, onContextWindowClose)
        }
    }
}
