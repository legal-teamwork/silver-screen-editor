package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class ResourceManager {

    val buttonId = mutableStateOf(0)
    val buttons = listOf(
        MenuButton("Sources", 0),
        MenuButton("Effects", 1),
        MenuButton("Presets", 2),
        MenuButton("Templates", 3),
    )

    @Composable
    fun compose() {
        Row(modifier = Modifier.background(color = Color(0xFF444444)).fillMaxSize()) {
            Menu()
            MainWindow()
        }
    }

    @Composable
    private fun MainWindow() {
    }

    companion object {
        val MENU_WIDTH = 200.dp
        val MENU_BUTTON_HEIGHT = 35.dp
    }
}
