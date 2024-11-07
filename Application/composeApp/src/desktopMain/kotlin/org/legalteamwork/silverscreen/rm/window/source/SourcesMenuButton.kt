package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.*

// Constants:
val IMAGE_WIDTH = 250.dp
val IMAGE_HEIGHT = 140.dp
val CELL_PADDING = 5.dp
val COLUMN_MIN_WIDTH = IMAGE_WIDTH + CELL_PADDING * 2

@Composable
fun SourcesMainWindow() {
    var contextWindow by remember { mutableStateOf<ContextWindow?>(null) }
    val onContextWindowOpen: (ContextWindow?) -> Unit = { contextWindow = it }
    val onContextWindowClose: () -> Unit = { contextWindow = null }

    BoxWithConstraints {
        Column {
            NavWindow(onContextWindowOpen, onContextWindowClose)
            SourcesPreviews(onContextWindowOpen, onContextWindowClose)
        }

        ContextWindow(contextWindow, onContextWindowOpen, onContextWindowClose)
    }
}

@Composable
fun NavWindow(
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = ResourceManager::onFolderUp
            ) {
                Text("Up")
            }

            Button(
                onClick = {
                    val contextWindowId = ContextWindow.ContextWindowId.NEW_FOLDER
                    val contextWindowData = ContextWindowData(ResourceManager.videoResources.value, Offset.Zero)
                    val contextWindow = ContextWindow(contextWindowId, contextWindowData)
                    onContextWindowOpen(contextWindow)
                }
            ) {
                Text("New folder")
            }

            Box(modifier = Modifier.wrapContentSize().background(Color.Magenta, shape = RoundedCornerShape(5.dp))) {
                Text(text = "Current folder: ${ResourceManager.videoResources.value.title.value}", modifier = Modifier.padding(10.dp))
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
