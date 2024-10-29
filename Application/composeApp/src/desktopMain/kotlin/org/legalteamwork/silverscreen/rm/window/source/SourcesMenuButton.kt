package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.Resource

// Constants:
val IMAGE_WIDTH = 250.dp
val IMAGE_HEIGHT = 140.dp
val CELL_PADDING = 5.dp
val COLUMN_MIN_WIDTH = IMAGE_WIDTH + CELL_PADDING * 2

@Composable
fun ResourceManager.SourcesMainWindow() {
    val resources = remember { videoResources }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = COLUMN_MIN_WIDTH)) {
            items(items = resources, key = Resource::hashCode) { resource ->
                SourcePreviewItem(resource)
            }

            item {
                SourceAddButton()
            }
        }
    }
}
