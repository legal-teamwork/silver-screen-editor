package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.rememberCursorPositionProvider

@Composable
fun ContextWindowScope.ResourceContextWindowPattern(
    content: @Composable () -> Unit,
) {
    val widthDp = 200F

    val popupPositionProvider = rememberCursorPositionProvider(
        offset = DpOffset(5.dp, 5.dp),
        alignment = Alignment.TopStart
    )

    Popup(
        popupPositionProvider = popupPositionProvider,
        onDismissRequest = { onContextWindowClose() },
    ) {
        val shape = RoundedCornerShape(5.dp)

        Box(
            modifier = Modifier
                .wrapContentSize()
                .width(widthDp.dp)
                .wrapContentHeight()
                .shadow(5.dp, shape = shape)
                .background(Color(0xFF222222), shape = shape)
                .border(1.dp, Color.LightGray, shape = shape)
        ) {
            content()
        }
    }
}
