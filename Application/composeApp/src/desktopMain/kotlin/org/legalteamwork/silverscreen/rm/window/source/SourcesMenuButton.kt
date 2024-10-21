package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.SimpleResource
import kotlin.math.max

// Constants:
val COLUMN_MIN_WIDTH = 250.dp
val ROW_HEIGHT = 200.dp
val CELL_PADDING = 10.dp

@Composable
fun ResourceManager.SourcesMainWindow() {
    val resources = remember { videoResources }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val columnsNumber = max((maxWidth / COLUMN_MIN_WIDTH).toInt(), 1)

        LazyVerticalGrid(columns = GridCells.Fixed(columnsNumber)) {
            items(items = resources) { resource ->
                SourcePreviewItem(resource)
            }

            item {
                SourceAddButton()
            }
        }
    }
}

@Composable
private fun SourcePreviewItem(resource: SimpleResource) {
    BoxWithConstraints(
        modifier = Modifier.height(ROW_HEIGHT).fillMaxSize().border(0.5.dp, Color(0x44000000)).padding(CELL_PADDING)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val textHeight = 20.dp
            val spaceBetween = 3.dp
            val imageHeight = this@BoxWithConstraints.maxHeight - textHeight - spaceBetween

            Image(
                painter = painterResource(resource.previewPath),
                contentDescription = resource.title,
                modifier = Modifier.height(imageHeight).fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )
            Text(
                text = resource.title,
                modifier = Modifier.height(textHeight).wrapContentSize(align = Alignment.BottomCenter),
                color = Color.White,
            )
        }
    }
}

@Composable
private fun ResourceManager.SourceAddButton() {
    val resources = remember { videoResources }

    Box(
        modifier = Modifier
            .height(ROW_HEIGHT)
            .fillMaxSize()
            .border(0.5.dp, Color(0x44000000))
            .padding(CELL_PADDING)
            .clickable(
                onClickLabel = "Add resource",
                role = Role.Image,
                onClick = ::addSourceTriggerActivity,
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource("svg/add-resource.svg"),
            contentDescription = "Add resource button",
            modifier = Modifier.fillMaxHeight(0.8f),
            contentScale = ContentScale.Fit
        )
    }
}
