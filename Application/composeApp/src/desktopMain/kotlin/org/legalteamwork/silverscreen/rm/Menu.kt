package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.legalteamwork.silverscreen.rm.ResourceManager.Companion.MENU_BUTTON_HEIGHT

class Menu {

    private val buttons = listOf(
        MenuButton("Sources"),
        MenuButton("Effects"),
        MenuButton("Presets"),
        MenuButton("Templates")
    )

    @Composable
    fun buttonList() {
        var chosenButton by remember { mutableStateOf(0) }

        for (buttonWithIndex in buttons.withIndex()) {
            val value = buttonWithIndex.value
            val index = buttonWithIndex.index

            menuButton(value.title, chosenButton != index) {
                chosenButton = index
            }
        }
    }

    @Composable
    private fun menuButton(title: String, enabled: Boolean, onClick: () -> Unit) {
        val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF3A3A3A),
            contentColor = Color.White,
            disabledBackgroundColor = Color(0xFF222222),
            disabledContentColor = Color.White,
        )

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
            colors = buttonColors,
            elevation = null,
            border = null,
            enabled = enabled
        ) {
            Text(title)
        }
    }

}