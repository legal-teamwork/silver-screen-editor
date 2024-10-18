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
        Box(modifier = Modifier.background(color = Color(0xFF3A3A3A)).fillMaxHeight().width(MENU_WIDTH)) {
            Column(modifier = Modifier.padding(5.dp)) {
                var sourcesEnabled by remember { mutableStateOf(false) }
                var effectsEnabled by remember { mutableStateOf(true) }
                var presetsEnabled by remember { mutableStateOf(true) }
                var templatesEnabled by remember { mutableStateOf(true) }
                val colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF3A3A3A),
                    contentColor = Color.White,
                    disabledBackgroundColor = Color(0xFF222222),
                    disabledContentColor = Color.White,
                )

                Button(
                    onClick = {
                        sourcesEnabled = false
                        effectsEnabled = true
                        presetsEnabled = true
                        templatesEnabled = true
                    },
                    modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
                    colors = colors,
                    elevation = null,
                    border = null,
                    enabled = sourcesEnabled
                ) {
                    Text("Sources")
                }
                Button(
                    onClick = {
                        sourcesEnabled = true
                        effectsEnabled = false
                        presetsEnabled = true
                        templatesEnabled = true
                    },
                    modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
                    colors = colors,
                    elevation = null,
                    border = null,
                    enabled = effectsEnabled
                ) {
                    Text("Effects")
                }
                Button(
                    onClick = {
                        sourcesEnabled = true
                        effectsEnabled = true
                        presetsEnabled = false
                        templatesEnabled = true
                    },
                    modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
                    colors = colors,
                    elevation = null,
                    border = null,
                    enabled = presetsEnabled
                ) {
                    Text("Presets")
                }
                Button(
                    onClick = {
                        sourcesEnabled = true
                        effectsEnabled = true
                        presetsEnabled = true
                        templatesEnabled = false
                    },
                    modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
                    colors = colors,
                    elevation = null,
                    border = null,
                    enabled = templatesEnabled
                ) {
                    Text("Templates")
                }
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
