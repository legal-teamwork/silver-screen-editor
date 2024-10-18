package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
                Button(
                    onClick = ::menuButtonClick,
                    modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
                    border = null,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF3A3A3A),
                        contentColor = Color.White,
                        disabledBackgroundColor = Color(0xFF222222),
                        disabledContentColor = Color.White,
                    ),
                    elevation = null,
                    enabled = false
                ) {
                    Text("Sources")
                }
                Button(
                    onClick = ::menuButtonClick,
                    modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
                    border = null,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF3A3A3A),
                        contentColor = Color.White,
                        disabledBackgroundColor = Color(0xFF222222),
                        disabledContentColor = Color.White,
                    ),
                    elevation = null,
                    enabled = true,
                ) {
                    Text("Effects")
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
