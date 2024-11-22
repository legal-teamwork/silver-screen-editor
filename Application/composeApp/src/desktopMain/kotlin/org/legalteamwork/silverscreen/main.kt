package org.legalteamwork.silverscreen

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.SaveManager
import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import org.legalteamwork.silverscreen.shortcut.ShortcutManager
import java.awt.Dimension
import java.awt.Toolkit

private val logger = KotlinLogging.logger {  }

fun main() {
    logger.info { "Program started!" }
    SaveManager.load()

    application {
        val icon = painterResource("icon.ico")
        Window(
            state = WindowState(WindowPlacement.Maximized),
            onCloseRequest = {
                SaveManager.save()
                exitApplication()
                logger.info { "Program finished\n\n" } },
            title = Strings.TITLE,
            icon = icon,
            onKeyEvent = ShortcutManager::onKeyEvent
        ) {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            window.minimumSize = Dimension(screenSize.width / 2, screenSize.height / 2)

            App()
        }
    }
}