package org.legalteamwork.silverscreen

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.legalteamwork.silverscreen.rm.Data
import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.resource.VideoResource

fun main() {
    //Test

    VideoEditor.addTrack()
    VideoEditor.addTrack()

    val testResource1 = VideoResource("path", "title", 100)
    val testResource2 = VideoResource("path", "title", 200)
    val testResource3 = VideoResource("path", "title", 150)

    VideoEditor.addResource(testResource1)
    VideoEditor.addResource(testResource2)
    VideoEditor.addResource(testResource3)
    VideoEditor.addResource(testResource3, 1)


    //End test


    Data.generateAllResources()

    application {
        val icon = painterResource("icon.ico")
        Window(
            state = WindowState(WindowPlacement.Maximized),
            onCloseRequest = {
                Data.saveVideoResourceData()
                exitApplication() },
            title = "Silver Screen Editor",
            icon = icon
        ) {
            App()
        }
    }
}