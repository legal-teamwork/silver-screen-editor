package org.legalteamwork.silverscreen

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.SaveManager
import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import java.awt.Dimension
import java.awt.Toolkit

fun main() {
    //Test

    VideoEditor.addTrack()
    VideoEditor.addTrack()

    val testResource1 = VideoResource("path", "title", ResourceManager.rootFolder, 100)
    val testResource2 = VideoResource("path", "title", ResourceManager.rootFolder, 200)
    val testResource3 = VideoResource("path", "title", ResourceManager.rootFolder, 150)

    VideoEditor.addResource(testResource1)
    VideoEditor.addResource(testResource2)
    VideoEditor.addResource(testResource3)
    VideoEditor.addResource(testResource3, 1)


    //End test


    SaveManager.load()

    application {
        val icon = painterResource("icon.ico")
        Window(
            state = WindowState(WindowPlacement.Maximized),
            onCloseRequest = {
                SaveManager.save()
                exitApplication() },
            title = "Silver Screen Editor",
            icon = icon
        ) {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            window.minimumSize = Dimension(screenSize.width / 2, screenSize.height / 2)
            App()
        }
    }
}