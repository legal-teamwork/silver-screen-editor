package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.Resource
import java.io.File
import kotlin.math.max

// Constants:
val IMAGE_WIDTH = 250.dp
val IMAGE_HEIGHT = 140.dp
val CELL_PADDING = 5.dp
val COLUMN_MIN_WIDTH = IMAGE_WIDTH + CELL_PADDING * 2

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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun SourcePreviewItem(resource: Resource) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().padding(CELL_PADDING)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var rememberedTitle by remember { resource.title }

            Image(
                painter = BitmapPainter(remember {
                    File(resource.previewPath).inputStream().readAllBytes().decodeToImageBitmap()
                }),
                contentDescription = rememberedTitle,
                modifier = Modifier.size(IMAGE_WIDTH, IMAGE_HEIGHT),
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.TopCenter
            )

            BasicTextField(
                value = rememberedTitle,
                onValueChange = { rememberedTitle = it },
                modifier = Modifier
                    .padding(top = 5.dp) // outer padding
                    .wrapContentSize(align = Alignment.BottomCenter)
                    .border(1.dp, SolidColor(Color.Black), RoundedCornerShape(2.dp))
                    .padding(5.dp),
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                cursorBrush = SolidColor(Color.Magenta),
            )
        }
    }
}

@Composable
private fun ResourceManager.SourceAddButton() {
    Box(
        modifier = Modifier.padding(CELL_PADDING).fillMaxSize()
    ) {
        Box(
            Modifier
                .size(IMAGE_WIDTH, IMAGE_HEIGHT)
                .align(Alignment.Center)
                .clickable(
                    onClickLabel = "Add resource",
                    role = Role.Image,
                    onClick = ::addSourceTriggerActivity,
                ),
        ) {
            Image(
                painter = painterResource("svg/add-resource.svg"),
                contentDescription = "Add resource button",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
            )
        }
    }
}
