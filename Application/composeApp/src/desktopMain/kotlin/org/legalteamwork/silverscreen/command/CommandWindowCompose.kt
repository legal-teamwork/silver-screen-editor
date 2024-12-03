package org.legalteamwork.silverscreen.command

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { commandManager.seek(0) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .padding(Dimens.COMMANDS_ICON_START_OFFSET, Dimens.COMMANDS_ICON_END_OFFSET)
                            .size(Dimens.COMMANDS_ICON_SIZE)
                            .background(
                                CommandWindowTheme.COMMANDS_ICON_MAJOR_COLOR
                            )

                    )
                    Text(
                        text = "Reset",
                        color = CommandWindowTheme.TEXT_COLOR
                    )
                }
            }
            itemsIndexed(commandManager.stack, key = { _, item -> item.hashCode() }) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { commandManager.seek(index + 1) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .padding(Dimens.COMMANDS_ICON_START_OFFSET, Dimens.COMMANDS_ICON_END_OFFSET)
                            .size(Dimens.COMMANDS_ICON_SIZE)
                            .background(
                                if (index < commandManager.pointer.value) CommandWindowTheme.COMMANDS_ICON_MAJOR_COLOR
                                else CommandWindowTheme.COMMANDS_ICON_MINOR_COLOR
                            )

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