package org.legalteamwork.silverscreen

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.resource.VideoResource

fun main() {

    application {
        val icon = painterResource("icon.ico")
        Window(
            state = WindowState(WindowPlacement.Maximized),
            onCloseRequest = ::exitApplication,
            title = "Silver Screen Editor",
            icon = icon
        ) {
            App()
        }
    }
}