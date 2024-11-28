package org.legalteamwork.silverscreen.command

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.opencv.presets.opencv_core.Str
import org.legalteamwork.silverscreen.ps.ProjectSettingsWindow
import org.legalteamwork.silverscreen.resources.Strings

class ProjectSettingsOpenMenuCommand : Command {

    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog(Strings.PROJECT_SETTINGS_ITEM)

        ProjectSettingsWindow.open()
    }
}