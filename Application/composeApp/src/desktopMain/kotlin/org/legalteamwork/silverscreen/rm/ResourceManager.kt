package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class ResourceManager {

    val buttonId = mutableStateOf(INIT_ID)
    val buttons = listOf(
        MenuButton(SOURCES_ID, "Sources"),
        MenuButton(EFFECTS_ID, "Effects"),
        MenuButton(PRESETS_ID, "Presets"),
        MenuButton(TEMPLATES_ID, "Templates"),
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
        val id by remember { buttonId }

        when (id) {
            SOURCES_ID -> SourcesMenuButton()
            else -> SimpleMenuButton()
        }
    }

    companion object {
        val MENU_WIDTH = 200.dp
        val MENU_BUTTON_HEIGHT = 35.dp
        const val INIT_ID = 1
        const val SOURCES_ID = 1
        const val EFFECTS_ID = 2
        const val PRESETS_ID = 3
        const val TEMPLATES_ID = 4
    }
}
