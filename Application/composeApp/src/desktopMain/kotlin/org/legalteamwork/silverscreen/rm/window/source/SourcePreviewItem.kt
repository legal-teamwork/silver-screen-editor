package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.rm.resource.Resource
import java.io.File

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun SourcePreviewItem(
    resource: Resource,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit = { onContextWindowOpen(null) }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CELL_PADDING)
            .onClick(
                enabled = true,
                matcher = PointerMatcher.mouse(PointerButton.Secondary),
                onClick = {
                    onContextWindowOpen(ContextWindow(ContextWindow.CONTEXT_MENU, resource))
                }
            ),
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
