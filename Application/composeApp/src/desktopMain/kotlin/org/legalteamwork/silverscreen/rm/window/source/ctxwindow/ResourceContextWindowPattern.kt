package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition
import androidx.compose.ui.zIndex


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResourceContextWindowPattern(
    position: Offset,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit = { onContextWindowOpen(null) },
    content: @Composable () -> Unit,
) {
    val widthDp = 200F
    val popupPositionProvider = rememberPopupPositionProviderAtPosition(position)

    Popup(
        popupPositionProvider = popupPositionProvider,
        onDismissRequest = onContextWindowClose,
    ) {
        Box(modifier = Modifier.wrapContentSize().zIndex(1f)) {
            Column(Modifier.width(widthDp.dp)) {
                Box(modifier = Modifier.fillMaxWidth().background(Color.DarkGray)) {
                    content()
                }
            }
        }
    }
}
