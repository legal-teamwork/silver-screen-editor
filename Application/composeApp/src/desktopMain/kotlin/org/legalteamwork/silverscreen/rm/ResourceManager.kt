package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class ResourceManager {

    private val buttonIndex = mutableStateOf(0)
    private val buttonList = listOf(
        MenuButton("Sources"),
        MenuButton("Effects"),
        MenuButton("Presets"),
        MenuButton("Templates"),
    )

    @Composable
    fun resourceManager() {
        Row(modifier = Modifier.background(color = Color(0xFF444444)).fillMaxSize()) {
            menu()
            mainWindow()
        }
    }

    @Composable
    private fun menu() {
        Box(modifier = Modifier.background(color = Color(0xFF3A3A3A)).fillMaxHeight().width(MENU_WIDTH)) {
            buttonList()
        }
    }

    @Composable
    fun buttonList() {
        Column(modifier = Modifier.padding(5.dp)) {
            for (buttonWithIndex in buttonList.withIndex()) {
                val value = buttonWithIndex.value
                val index = buttonWithIndex.index

                menuButton(value.title, index)
            }
        }
    }

    @Composable
    private fun menuButton(title: String, index: Int) {
        var chosenButton by remember { buttonIndex }
        val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF3A3A3A),
            contentColor = Color.White,
            disabledBackgroundColor = Color(0xFF222222),
            disabledContentColor = Color.White,
        )

        Button(
            onClick = {
                chosenButton = index
            },
            modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
            colors = buttonColors,
            elevation = null,
            border = null,
            enabled = chosenButton != index
        ) {
            Text(title)
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
