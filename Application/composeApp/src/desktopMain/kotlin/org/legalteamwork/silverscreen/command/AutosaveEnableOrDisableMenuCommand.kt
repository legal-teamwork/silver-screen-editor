package org.legalteamwork.silverscreen.command

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.opencv.presets.opencv_core.Str
import org.legalteamwork.silverscreen.ps.ProjectSettingsWindow
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.save.EditorSettings

class AutosaveEnableOrDisableMenuCommand : Command {

    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog("${Strings.FILE_AUTO_SAVE_ITEM_ON}/${Strings.FILE_AUTO_SAVE_ITEM_OFF}")

        EditorSettings.change {
            autosaveEnabled.value = !autosaveEnabled.value
        }

        EditorSettings.save()
    }
}