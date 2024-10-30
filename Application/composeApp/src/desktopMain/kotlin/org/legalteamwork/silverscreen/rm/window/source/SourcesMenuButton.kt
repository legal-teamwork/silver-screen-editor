package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ContextWindow
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ResourceActionsContextWindow
import org.legalteamwork.silverscreen.rm.window.source.ctxwindow.ResourcePropertiesContextWindow

// Constants:
val IMAGE_WIDTH = 250.dp
val IMAGE_HEIGHT = 140.dp
val CELL_PADDING = 5.dp
val COLUMN_MIN_WIDTH = IMAGE_WIDTH + CELL_PADDING * 2

@Composable
fun SourcesMainWindow() {
    val resources = remember { ResourceManager.videoResources }
    val contextWindowState = mutableStateOf<ContextWindow?>(null)
    var contextWindow by remember { contextWindowState }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = COLUMN_MIN_WIDTH)) {
            items(items = resources, key = Resource::hashCode) { resource ->
                SourcePreviewItem(
                    resource = resource,
                    onContextWindowOpen = { contextWindow = it }
                )
            }

            item {
                SourceAddButton()
            }
        }
    }

    contextWindow?.apply {
        when (id) {
            ContextWindow.CONTEXT_MENU -> ResourceActionsContextWindow(
                data,
                onContextWindowOpen = { contextWindow = it }
            )

            ContextWindow.PROPERTIES -> ResourcePropertiesContextWindow(data)
        }
    }
}
