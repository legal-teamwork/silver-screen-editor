package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


@Composable
fun ResourceContextWindowPattern(
    position: Offset,
    parentConstraints: Constraints,
    dragLineColor: Color = Color.Blue,
    content: @Composable () -> Unit
) {
    val widthDp = 300F
    val rightPosition = position + Offset(widthDp, 0F)
    val modifiedPosition = if (rightPosition.x > parentConstraints.maxWidth) {
        Offset(parentConstraints.maxWidth - widthDp, position.y)
    } else {
        position
    }

    Surface(
        modifier = Modifier.wrapContentSize().zIndex(1f).offset(modifiedPosition.x.dp, modifiedPosition.y.dp),
        color = Color.Transparent,
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp,
    ) {
        Column(Modifier.width(widthDp.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(dragLineColor))

            Box(modifier = Modifier.fillMaxWidth().background(Color.DarkGray)) {
                content()
            }
        }
    }
}
