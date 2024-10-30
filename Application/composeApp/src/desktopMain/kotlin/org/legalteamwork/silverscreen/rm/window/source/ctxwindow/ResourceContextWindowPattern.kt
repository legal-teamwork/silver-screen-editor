package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


@Composable
fun ResourceContextWindowPattern(
    position: Offset,
    dragLineColor: Color = Color.Blue,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.wrapContentSize().zIndex(1f).offset(position.x.dp, position.y.dp),
        color = Color.Transparent,
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp,
    ) {
        Column(Modifier.width(300.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(dragLineColor))

            Box(modifier = Modifier.fillMaxWidth().background(Color.DarkGray)) {
                content()
            }
        }
    }
}
