package org.legalteamwork.silverscreen.rm.window

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorMenuButton() {
    BasicText(text = "ERROR WINDOW", color = { Color.Red })
}
