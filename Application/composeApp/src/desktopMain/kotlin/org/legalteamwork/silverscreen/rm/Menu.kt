package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager.Companion.MENU_BUTTON_HEIGHT
import org.legalteamwork.silverscreen.rm.ResourceManager.Companion.MENU_WIDTH

@Composable
fun ResourceManager.Menu() {
    Box(modifier = Modifier.background(color = Color(0xFF3A3A3A), RoundedCornerShape(8.dp)).fillMaxHeight().width(MENU_WIDTH)) {
        ButtonList()
    }
}

@Composable
private fun ResourceManager.ButtonList() {
    Column(modifier = Modifier.padding(5.dp)) {
        for (button in buttons) {
            MenuButton(button)
        }
    }
}

@Composable
private fun ResourceManager.MenuButton(button: MenuButton) {
    var chosenButton by remember { buttonId }
    val buttonColors =
        ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF3A3A3A),
            contentColor = Color.White,
            disabledBackgroundColor = Color(0xFF222222),
            disabledContentColor = Color.White,
        )

    Button(
        onClick = {
            chosenButton = button.id
        },
        modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
        colors = buttonColors,
        elevation = null,
        border = null,
        enabled = chosenButton != button.id,
    ) {
        Text(button.title)
    }
}
