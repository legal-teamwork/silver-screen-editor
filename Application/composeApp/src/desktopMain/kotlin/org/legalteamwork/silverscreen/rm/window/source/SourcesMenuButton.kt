package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.resource.SimpleResource
import kotlin.math.max

// Constants:
val COLUMN_MIN_WIDTH = 250.dp
val IMAGE_MAX_HEIGHT = 215.dp

val resources = listOf(
    SimpleResource("Untitled1.mp4", "tmp-resources/u1.png"),
    SimpleResource("Untitled2.mp4", "tmp-resources/u2.png"),
    SimpleResource("Untitled3.mp4", "tmp-resources/u3.png"),
    SimpleResource("Untitled1.mp4", "tmp-resources/u1.png"),
    SimpleResource("Untitled2.mp4", "tmp-resources/u2.png"),
    SimpleResource("Untitled1.mp4", "tmp-resources/u1.png"),
    SimpleResource("Untitled2.mp4", "tmp-resources/u2.png"),
)

@Composable
fun SourcesMainWindow() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val columnsNumber = max((maxWidth / COLUMN_MIN_WIDTH).toInt(), 1)

        LazyVerticalGrid(columns = GridCells.Fixed(columnsNumber)) {
            items(resources) { resource ->
                SourcePreviewItem(resource)
            }
        }
    }
}

@Composable
private fun SourcePreviewItem(resource: SimpleResource) {
    BoxWithConstraints {
        val boxWithConstraintsState = this

        Column(
            modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(resource.previewPath),
                contentDescription = resource.title,
                modifier = Modifier.heightIn(0.dp, IMAGE_MAX_HEIGHT).widthIn(0.dp, boxWithConstraintsState.maxWidth),
                contentScale = ContentScale.Fit
            )
            BasicText(
                text = resource.title, color = { Color.White }, modifier = Modifier.padding(top = 3.dp)
            )
        }
    }
}
