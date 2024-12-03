package org.legalteamwork.silverscreen.command.menu

import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.Command
import org.legalteamwork.silverscreen.command.commandLog
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.save.EditorSettings

class AutosaveEnableOrDisableCommand : Command {
    override val title: String =
        "${Strings.FILE_AUTO_SAVE_ITEM_ON}/${Strings.FILE_AUTO_SAVE_ITEM_OFF}"
    override val description: String =
        "${Strings.FILE_AUTO_SAVE_ITEM_ON}/${Strings.FILE_AUTO_SAVE_ITEM_OFF}"
    private val logger = KotlinLogging.logger {}

    override fun execute() {
        logger.commandLog("${Strings.FILE_AUTO_SAVE_ITEM_ON}/${Strings.FILE_AUTO_SAVE_ITEM_OFF}")

        EditorSettings.change {
            autosaveEnabled.value = !autosaveEnabled.value
        }

        EditorSettings.save()
    }
}