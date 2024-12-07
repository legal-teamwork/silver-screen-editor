package org.legalteamwork.silverscreen.command.menu

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.Command
import org.legalteamwork.silverscreen.command.commandLog
import org.legalteamwork.silverscreen.ps.ProjectSettingsWindow
import org.legalteamwork.silverscreen.resources.Strings

class ProjectSettingsOpenCommand : Command {
    override val title: String = Strings.PROJECT_SETTINGS_ITEM
    override val description: String = Strings.PROJECT_SETTINGS_ITEM
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog(Strings.PROJECT_SETTINGS_ITEM)

        ProjectSettingsWindow.open()
    }
}