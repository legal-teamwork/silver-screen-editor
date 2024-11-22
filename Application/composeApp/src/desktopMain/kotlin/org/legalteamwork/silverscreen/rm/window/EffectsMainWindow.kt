package org.legalteamwork.silverscreen.rm.window

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.legalteamwork.silverscreen.resources.MainWindowTheme

@Composable
fun EffectsMainWindow() {
    BasicText(text = "EFFECTS WINDOW", color = { MainWindowTheme.EFFECTS_MAIN_WINDOW_TEXT_COLOR })
}
