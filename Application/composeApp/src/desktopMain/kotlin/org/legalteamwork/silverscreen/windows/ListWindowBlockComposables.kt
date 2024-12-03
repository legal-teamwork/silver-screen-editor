package org.legalteamwork.silverscreen.windows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp

@Composable
fun ListWindowBlockDivider(
    width: Dp,
    height: Dp,
    pointerInput: suspend PointerInputScope.() -> Unit
) = Box(Modifier.size(width, height).pointerHoverIcon(PointerIcon.Hand).pointerInput(Unit, pointerInput))
