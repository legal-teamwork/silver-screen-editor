package org.legalteamwork.silverscreen

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.command.menu.*
import org.legalteamwork.silverscreen.menu.*
import org.legalteamwork.silverscreen.ps.*
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.save.EditorSettings
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.shortcut.Shortcut
import org.legalteamwork.silverscreen.shortcut.ShortcutManager
import java.awt.Dimension
import java.awt.Toolkit

private val logger = KotlinLogging.logger { }

private fun onStart() {
    EditorSettings.load()
    Project.autoload()
}

private fun onClose() {
    EditorSettings.save()
    Project.autosave()
}

private fun ShortcutManager.setUpMenuBarShortcuts(menuBarData: MenuBarData) {
    for (menuData in menuBarData.menuList) {
        if (menuData.mnemonic != null) {
            addShortcut(Shortcut(key = menuData.mnemonic, alt = true)) {
                val currentActiveMenu = menuBarData.activeMenu.value
                if (currentActiveMenu == menuData) {
                    // Close menu
                    menuBarData.activeMenu.component2().invoke(null)
                } else {
                    // Open menu
                    menuBarData.activeMenu.component2().invoke(menuData)
                }

                true
            }
        }

        for (menuItemData in menuData.menuItemList) {
            if (menuItemData.shortcut != null) {
                addShortcut(menuItemData.shortcut) {
                    menuItemData.onClick()
                    true
                }
            }
        }
    }
}

private fun ShortcutManager.setUpSystemShortcuts(commandManager: CommandManager) {
    addShortcut(Shortcut(Key.Z, ctrl = true)) {
        commandManager.undo()
        true
    }
    addShortcut(Shortcut(Key.Z, ctrl = true, shift = true)) {
        commandManager.redo()
        true
    }
}

private fun initMenuBarData(commandManager: CommandManager, resourceManager: ResourceManager) = MenuBarData(
    activeMenu = null,
    MenuData(
        Strings.FILE_MENU_TAG,
        mnemonic = Key.F,
        enabled = true,
        MenuItemData({ Strings.FILE_NEW_ITEM }, shortcut = Shortcut(Key.N, ctrl = true)) {
            commandManager.execute(NewCommand())
        },
        MenuItemData({ Strings.FILE_OPEN_ITEM }, shortcut = Shortcut(Key.O, ctrl = true)) {
            commandManager.execute(OpenCommand())
        },
        MenuItemData({ Strings.FILE_IMPORT_ITEM }, shortcut = Shortcut(Key.I, ctrl = true)) {
            commandManager.execute(ImportCommand(resourceManager, commandManager))
        },
        MenuItemData({ Strings.FILE_EXPORT_ITEM }, shortcut = Shortcut(Key.R, ctrl = true, shift = true)) {
            commandManager.execute(ExportCommand())
        },
        MenuItemData({ Strings.FILE_SAVE_ITEM }, shortcut = Shortcut(Key.S, ctrl = true)) {
            commandManager.execute(SaveCommand())
        },
        MenuItemData({ Strings.FILE_SAVE_AS_ITEM }, shortcut = Shortcut(Key.S, ctrl = true, shift = true)) {
            commandManager.execute(SaveAsCommand())
        },
        MenuItemData({ Strings.PROJECT_SETTINGS_ITEM }, shortcut = Shortcut(Key.P, ctrl = true)) {
            commandManager.execute(ProjectSettingsOpenCommand())
        },
        MenuItemData(
            {
                if (EditorSettings.get().autosaveEnabled.value) Strings.FILE_AUTO_SAVE_ITEM_ON
                else Strings.FILE_AUTO_SAVE_ITEM_OFF
            },
            shortcut = Shortcut(Key.E, ctrl = true, shift = true)
        ) {
            commandManager.execute(AutosaveEnableOrDisableCommand())
        })
)

private fun initShortcutManager(commandManager: CommandManager, menuBarData: MenuBarData): ShortcutManager {
    val shortcutManager = ShortcutManager()

    shortcutManager.setUpMenuBarShortcuts(menuBarData)
    shortcutManager.setUpSystemShortcuts(commandManager)

    return shortcutManager
}

fun main() {
    logger.info { "Program started!" }
    onStart()

    val commandManager = CommandManager()
    val resourceManager = ResourceManager
    val menuBarData = initMenuBarData(commandManager, resourceManager)
    val shortcutManager = initShortcutManager(commandManager, menuBarData)
    val appScope = AppScope(commandManager, resourceManager, shortcutManager, menuBarData)

    application {
        val icon = painterResource("icon.ico")
        Window(
            state = WindowState(WindowPlacement.Maximized), onCloseRequest = {
                onClose()
                exitApplication()
                logger.info { "Program finished\n\n" }
            }, title = Strings.TITLE, icon = icon, onKeyEvent = shortcutManager::onKeyEvent
        ) {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            window.minimumSize = Dimension(screenSize.width / 2, screenSize.height / 2)

            appScope.App()
        }

        if (ProjectSettingsWindow.isOpened) {
            Window(
                title = Strings.PROJECT_SETTINGS_TITLE, icon = icon, onCloseRequest = {
                    logger.info { "Project settings window closed" }
                    ProjectSettingsWindow.close()
                }) {
                ProjectSettingsWindow.compose()
            }
        }
    }
}
