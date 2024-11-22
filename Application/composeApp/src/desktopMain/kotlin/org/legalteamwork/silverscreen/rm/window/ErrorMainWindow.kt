package org.legalteamwork.silverscreen.rm.window

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.legalteamwork.silverscreen.resources.MainWindowTheme

@Composable
fun ErrorMainWindow() {
    BasicText(text = "ERROR WINDOW", color = { MainWindowTheme.ERROR_MAIN_WINDOW_TEXT_COLOR })
}
