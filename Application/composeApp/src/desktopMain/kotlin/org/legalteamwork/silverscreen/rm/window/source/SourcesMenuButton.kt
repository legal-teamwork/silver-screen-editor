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
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.*
import silverscreeneditor.composeapp.generated.resources.Res
import silverscreeneditor.composeapp.generated.resources.add_folder
import silverscreeneditor.composeapp.generated.resources.up

// Constants:
val IMAGE_WIDTH = 250.dp
val IMAGE_HEIGHT = 140.dp
val CELL_PADDING = 5.dp
val COLUMN_MIN_WIDTH = IMAGE_WIDTH + CELL_PADDING * 2
val NAV_ICON_SIZE = 40.dp

@Composable
fun SourcesMainWindow() {
    var contextWindow by remember { mutableStateOf<ContextWindow?>(null) }
    val onContextWindowOpen: (ContextWindow?) -> Unit = { contextWindow = it }
    val onContextWindowClose: () -> Unit = { contextWindow = null }

    BoxWithConstraints {
        Column {
            NavWindow(onContextWindowOpen, onContextWindowClose)
            PathWindow()
            SourcesPreviews(onContextWindowOpen, onContextWindowClose)
        }

        ContextWindow(contextWindow, onContextWindowOpen, onContextWindowClose)
    }
}

@Composable
fun PathWindow() {
    Box(Modifier.fillMaxWidth().wrapContentHeight().background(Color(0xFF3A3A3A))) {
        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(5.dp),
        ) {
            val path = mutableListOf<FolderResource>()
            var current: FolderResource? = ResourceManager.videoResources.value

            while (current != null) {
                path.add(current)
                current = current.parent
            }

            val pathText = path.reversed().joinToString("/", prefix = "/", postfix = "/") { it.title.value }

            Text(
                text = pathText,
                modifier = Modifier.padding(5.dp),
                color = Color.White
            )
        }
    }
}

@Composable
fun NavWindow(
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    Box(Modifier.fillMaxWidth().wrapContentHeight().background(Color(0xFF3A3A3A))) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
                    .clickable {
                        ResourceManager.onFolderUp()
                    }
            ) {
                Image(
                    painter = painterResource(Res.drawable.up),
                    contentDescription = "Up",
                    modifier = Modifier.size(NAV_ICON_SIZE).padding(5.dp)
                )
            }

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
                    .clickable {
                        val contextWindowId = ContextWindow.ContextWindowId.NEW_FOLDER
                        val contextWindowData = ContextWindowData(ResourceManager.videoResources.value, Offset.Zero)
                        val contextWindow = ContextWindow(contextWindowId, contextWindowData)
                        onContextWindowOpen(contextWindow)
                    }
            ) {
                Image(
                    painter = painterResource(Res.drawable.add_folder),
                    contentDescription = "Add folder",
                    modifier = Modifier.size(NAV_ICON_SIZE).padding(5.dp)
                )
            }
        }
    }
}

@Composable
private fun SourcesPreviews(
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = COLUMN_MIN_WIDTH)) {
            items(
                items = ResourceManager.videoResources.value.resources.toList().sortedBy {
                    if (it is FolderResource) {
                        "0 ${it.title.value}"
                    } else {
                        "1 ${it.title.value}"
                    }
                },
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

@Composable
private fun ContextWindow(
    contextWindow: ContextWindow?,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    contextWindow?.apply {
        when (id) {
            ContextWindow.ContextWindowId.CONTEXT_MENU -> ResourceActionsContextWindow(
                data,
                onContextWindowOpen,
                onContextWindowClose
            )

            ContextWindow.ContextWindowId.PROPERTIES -> ResourcePropertiesContextWindow(
                data,
                onContextWindowOpen,
                onContextWindowClose
            )

            ContextWindow.ContextWindowId.MOVE_TO -> MoveToWindow(data, onContextWindowOpen, onContextWindowClose)
            ContextWindow.ContextWindowId.COPY_TO -> CopyToWindow(data, onContextWindowOpen, onContextWindowClose)
            ContextWindow.ContextWindowId.NEW_FOLDER -> NewFolderWindow(data, onContextWindowOpen, onContextWindowClose)
        }
    }
}
