package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProviderAtPosition

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoveToWindow(
    contextWindowData: ContextWindowData,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit,
): Nothing = TODO()
//    val resource = contextWindowData.resource
//    val position = contextWindowData.position
//    val widthDp = 400F
//    val shape = RoundedCornerShape(5.dp)
//
//    Popup(
//        popupPositionProvider = PopupPositionProviderAtPosition(position, true, Offset.Zero, Alignment.BottomEnd, 4),
//        onDismissRequest = onContextWindowClose,
//    ) {
//        Box(
//            modifier = Modifier
//                .wrapContentSize()
//                .width(widthDp.dp)
//                .wrapContentHeight()
//                .shadow(5.dp, shape = shape)
//                .background(Color.DarkGray, shape = shape)
//                .border(1.dp, Color.LightGray, shape = shape)
//        ) {
//            Box(modifier = Modifier.fillMaxWidth()) {
//
//            }
//        }
//    }
//}