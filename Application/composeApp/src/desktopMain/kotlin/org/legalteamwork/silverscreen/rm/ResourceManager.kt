package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class ResourceManager {

    @Composable
    fun resourceManager() {
        Row(modifier = Modifier.background(color = Color(0xFF444444)).fillMaxSize()) {
            menu()
            mainWindow()
        }
    }

    private fun menuButtonClick() {
        // TODO
    }

    @Composable
    private fun menu() {
        val menu = Menu()

        Box(modifier = Modifier.background(color = Color(0xFF3A3A3A)).fillMaxHeight().width(MENU_WIDTH)) {
            Column(modifier = Modifier.padding(5.dp)) {
                menu.buttonList()
            }
        }
    }

    @Composable
    private fun mainWindow() {
    }

    companion object {
        val MENU_WIDTH = 200.dp
        val MENU_BUTTON_HEIGHT = 35.dp
    }
}
