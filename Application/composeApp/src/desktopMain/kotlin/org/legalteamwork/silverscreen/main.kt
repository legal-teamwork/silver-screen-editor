package org.legalteamwork.silverscreen

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.save.EditorSettingsSaveManager
import org.legalteamwork.silverscreen.save.ProjectSaveManager
import org.legalteamwork.silverscreen.shortcut.ShortcutManager
import java.awt.Dimension
import java.awt.Toolkit

private val logger = KotlinLogging.logger {  }

private fun onStart() {
    EditorSettingsSaveManager.load()
    ProjectSaveManager.autoload()
}

private fun onClose() {
    EditorSettingsSaveManager.save()
    ProjectSaveManager.autosave()
}

fun main() {
    logger.info { "Program started!" }
    onStart()

    application {
        val icon = painterResource("icon.ico")
        Window(
            state = WindowState(WindowPlacement.Maximized),
            onCloseRequest = {
                onClose()
                exitApplication()
                logger.info { "Program finished\n\n" }
            },
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