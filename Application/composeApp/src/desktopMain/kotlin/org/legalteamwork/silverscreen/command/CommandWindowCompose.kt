package org.legalteamwork.silverscreen.command

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.CommandWindowTheme
import org.legalteamwork.silverscreen.resources.Dimens

@Composable
fun AppScope.CommandWindowCompose() {
    Column(Modifier.fillMaxSize().padding(Dimens.COMMAND_WINDOW_MARGIN)) {
        Text(
            text = "Commands Log",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = Dimens.HEADER_TEXT_SIZE,
            color = CommandWindowTheme.TEXT_COLOR
        )

        LazyColumn(Modifier.fillMaxSize()) {
            itemsIndexed(commandManager.stack, key = { _, item -> item.hashCode() }) { index, item ->
                Row {
                    Box(
                        Modifier
                            .size(10.dp)
                            .background(if (index < commandManager.pointer.value) Color.Green else Color.Gray)
                    )
                    Text(
                        text = item.title,
                        color = CommandWindowTheme.TEXT_COLOR
                    )
                }
            }
        }
    }
}