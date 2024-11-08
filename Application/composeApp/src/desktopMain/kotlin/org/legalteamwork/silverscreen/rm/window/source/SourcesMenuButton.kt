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
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.Resource
import java.io.File
import kotlin.math.max

// Constants:
val COLUMN_MIN_WIDTH = 250.dp
val ROW_HEIGHT = 200.dp
val CELL_PADDING = 10.dp

@Composable
fun ResourceManager.SourcesMainWindow() {
    val resources = remember { videoResources }

    Column {
        // Кнопка переключения режима
        Button(
            onClick = { toggleViewMode() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (isListView.value) "Switch to Icons" else "Switch to List")
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            if (isListView.value) {
                // Режим списка
                LazyColumn {
                    items(resources) { resource ->
                        ListViewItem(resource)
                    }
                    item {
                        ListAddButton()
                    }
                }
            } else {
                // Режим сетки (существующий код)
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
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ListViewItem(resource: Resource) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(0.5.dp, Color(0x44000000))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Миниатюра
        Image(
            painter = BitmapPainter(remember {
                File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
            }),
            contentDescription = resource.title,
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )

        // Название файла
        Text(
            text = resource.title,
            color = Color.White,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        )

        // Можно добавить дополнительную информацию
        Text(
            text = "Size: ${File(resource.previewPath).length() / 1024} KB",
            color = Color.Gray
        )
    }
}

@Composable
private fun ResourceManager.ListAddButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(0.5.dp, Color(0x44000000))
            .padding(8.dp)
            .clickable(
                onClickLabel = "Add resource",
                role = Role.Button,
                onClick = ::addSourceTriggerActivity
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource("svg/add-resource.svg"),
            contentDescription = "Add resource button",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Add New Resource",
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun SourcePreviewItem(resource: Resource) {
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
                painter = BitmapPainter(remember {
                    File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
                }),
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
