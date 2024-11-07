package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager
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
        SourcesPreviews(onContextWindowOpen, onContextWindowClose)
        ContextWindow(contextWindow, constraints, onContextWindowOpen, onContextWindowClose)
    }
}

@Composable
private fun SourcesPreviews(
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = COLUMN_MIN_WIDTH)) {
            items(items = ResourceManager.videoResources.value.resources.toList(), key = Resource::hashCode) { resource ->
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
    parentConstraints: Constraints,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    contextWindow?.apply {
        when (id) {
            ContextWindow.ContextWindowId.CONTEXT_MENU -> ResourceActionsContextWindow(data, onContextWindowOpen, onContextWindowClose)
            ContextWindow.ContextWindowId.PROPERTIES -> ResourcePropertiesContextWindow(data, onContextWindowOpen, onContextWindowClose)
            ContextWindow.ContextWindowId.MOVE_TO -> MoveToWindow(data, onContextWindowOpen, onContextWindowClose)
            ContextWindow.ContextWindowId.COPY_TO -> CopyToWindow(data, onContextWindowOpen, onContextWindowClose)
        }
    }
}
