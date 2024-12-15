package org.legalteamwork.silverscreen

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.command.edit.CutResourceOnTrackCommand
import org.legalteamwork.silverscreen.command.edit.DeleteResourcesOnTrackCommand
import org.legalteamwork.silverscreen.ps.*
import org.legalteamwork.silverscreen.re.Slider
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.re.VideoTrack
import org.legalteamwork.silverscreen.re.getHighlightedResources
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.window.effects.EffectsManager
import org.legalteamwork.silverscreen.save.EditorSettings
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.shortcut.Shortcut
import org.legalteamwork.silverscreen.shortcut.ShortcutManager
import org.legalteamwork.silverscreen.vp.VideoPanel
import java.awt.Dimension
import java.awt.Toolkit

private val logger = KotlinLogging.logger {  }

private fun onStart() {
    logger.info { "Program started!" }
    EditorSettings.load()
    Project.autoload()
}

private fun onClose() {
    EditorSettings.save()
    Project.autosave()
    logger.info { "Program finished\n\n" }
}

fun main() {
    val commandManager = CommandManager()
    val resourceManager = ResourceManager
    val shortcutManager = ShortcutManager
    val effectsManager = EffectsManager()
    val appScope = AppScope(commandManager, resourceManager, shortcutManager, effectsManager)

    shortcutManager.addShortcut(Shortcut(Key.Z, ctrl = true)) {
        commandManager.undo()
        true
    }
    shortcutManager.addShortcut(Shortcut(Key.Z, ctrl = true, shift = true)) {
        commandManager.redo()
        true
    }
    shortcutManager.addShortcut(Shortcut(Key.Delete)) {
        val highlightedResources = VideoEditor.getHighlightedResources()
        if (highlightedResources.size > 0) {
            commandManager.execute(DeleteResourcesOnTrackCommand(VideoTrack, highlightedResources))
        }
        else {
            logger.warn { "Del shortcut triggered, but there is nothing highlighted!" }
        }
        true
    }
    shortcutManager.addShortcut(Shortcut(Key.C, alt = true)) {
        if (VideoPanel.playbackManager.isPlaying.value)
            VideoPanel.playbackManager.pause()

        val position = Slider.getPosition()
        val index = VideoTrack.resourcesOnTrack.indexOfFirst{ it.isPosInside(position) }
        if (index != -1) {
            commandManager.execute(CutResourceOnTrackCommand(VideoTrack, position, index))
        }
        else {
            logger.warn { "Alt+C (cut) shortcut triggered, but there is nothing to cut!" }
        }
        true
    }


//    onStart()
    application {
        val icon = painterResource("icon.ico")
        Window(
            state = WindowState(WindowPlacement.Maximized),
            onCloseRequest = {
//                onClose()
                exitApplication()
            },
            title = Strings.TITLE,
            icon = icon,
            onKeyEvent = ShortcutManager::onKeyEvent
        ) {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            window.minimumSize = Dimension(screenSize.width / 2, screenSize.height / 2)

            appScope.App()
        }

        if (ProjectSettingsWindow.isOpened) {
            Window(
                title = Strings.PROJECT_SETTINGS_TITLE,
                icon = icon,
                onCloseRequest = {
                    logger.info { "Project settings window closed" }
                    ProjectSettingsWindow.close()
                }
            ) {
                ProjectSettingsWindow.compose()
            }
        }
    }
}