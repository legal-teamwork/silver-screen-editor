package org.legalteamwork.silverscreen.rm

import androidx.compose.runtime.*
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import java.awt.FileDialog
import java.awt.Frame
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.command.resource.AddResourceCommand
import org.legalteamwork.silverscreen.command.CommandManager
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.rm.ResourceManager.currentFolder

/**
 * Открывает диалоговое окно для выбора файлов
 *
 * @param[parent] parent window frame
 * @param[title] window title
 * @param[allowedExtensions] allowed extensions to pick, examples: ".jpg", ".mp4" etc.
 * @param[allowMultiSelection] true if user can pick multiple selections
 *
 * @return [Set] of chosen files
 */
fun openFileDialog(
    parent: Frame?, title: String, allowedExtensions: List<String>, allowMultiSelection: Boolean = true
) = FileDialog(parent, title, FileDialog.LOAD).apply {
    isMultipleMode = allowMultiSelection

    // windows
    file = allowedExtensions.joinToString(";") { "*.$it" } // e.g. '*.jpg'

    // linux
    setFilenameFilter { _, name ->
        allowedExtensions.any {
            name.endsWith(it)
        }
    }

    isVisible = true
}.files.toSet()

/**
 * Базовый класс для файлового менеджера, реализующий смену окон (нажатия на вкладки) и содержащий методы отрисовки всего окна.
 * Синглтон, потому что вкладка единственна
 */
object ResourceManager {
    // Logger
    private val logger = KotlinLogging.logger {}

    // Текущий выбранный тип ресурса для отображения
    val currentResourceType = mutableStateOf(ResourceType.ALL)

    /**
     * Структура кнопки ресурс-менеджера
     * @param id уникальный идентификатор кнопки
     * @param iconPath путь к SVG иконке
     * @param resourceType тип ресурса для отображения
     */
    data class ResourceButton(
        val id: Int,
        val iconPath: String,
        val resourceType: ResourceType,
        val name: String
    )

    // Кнопки ресурс-менеджера
    val resourceButtons = listOf(
        ResourceButton(1, "resource-manager-buttons/media_big_button.svg", ResourceType.ALL, "Media"),
        //ResourceButton(2, "resource-manager-buttons/video.svg", ResourceType.VIDEO),
        //ResourceButton(3, "resource-manager-buttons/music.svg", ResourceType.AUDIO),
        //ResourceButton(4, "resource-manager-buttons/image.svg", ResourceType.IMAGE),
        //ResourceButton(5, "resource-manager-buttons/text.svg", ResourceType.TEXT),
        ResourceButton(6, "resource-manager-buttons/effects_big_button.svg", ResourceType.EFFECTS, "Effects")
    )

    /** Tabs:
    val tabId = mutableStateOf(Dimens.INIT_ID)
    val tabs = listOf(
        MenuButton(Dimens.SOURCES_ID, Strings.SOURCES),
        MenuButton(Dimens.EFFECTS_ID, Strings.EFFECTS),
    ) */

    // Folder management:
    val rootFolder: FolderResource = FolderResource.createRoot()
    val currentFolder: MutableState<FolderResource> = mutableStateOf(rootFolder)
    val activeResource: MutableState<Resource?> = mutableStateOf(null)

    //Режимы отображения: список
    val isListView = mutableStateOf(false)

    fun toggleViewMode() {
        isListView.value = !isListView.value
    }


    /**
     * Устанавливает текущий тип ресурса для отображения
     * @param type тип ресурса из перечисления [ResourceType]
     */
    fun setResourceType(type: ResourceType) {
        currentResourceType.value = type
        logger.info { "Changed resource type to: $type" }
    }


    /**
     * Триггерит вызов окна с выбором ресурса с последующей обработкой и созранением в [currentFolder]
     */
    fun addSourceTriggerActivity(commandManager: CommandManager) {
        logger.info { "Triggering file picker function openFileDialog for video resources" }
        val loadFiles = openFileDialog(null, "File Picker", listOf("mp4"))

        if (loadFiles.isEmpty()) {
            logger.warn { "No files selected in file picker" }
        }
        else {
            logger.info { "Files selected: ${loadFiles.joinToString { it.path }}" }
            for (loadFile in loadFiles) {
                if (loadFile.extension == "mp4") {
                    val resource = VideoResource(loadFile.path, currentFolder.value)
                    val addResourceCommand = AddResourceCommand(this, resource)
                    commandManager.execute(addResourceCommand)
                }
                else {
                    logger.error { "${loadFile.path} has wrong extension: \"${loadFile.extension}\"! mp4 expected" }
                }
            }
        }
    }

    /**
     * Changes current showing folder to the parent one if it is possible
     */
    fun onFolderUp() {
        val parent = currentFolder.value.parent

        if (currentFolder.value != rootFolder && parent != null) {
            currentFolder.component2().invoke(parent)
            logger.info { "Navigated up to folder: ${parent.title.value}" }
        }
        else {
            logger.warn { "Cannot navigate up: already at the root folder or no parent." }
        }
    }

    /**
     * Gets path from the root folder to the provided one
     *
     * @param[folder] provided folder
     *
     * @return path as string like 'root/folder/'
     */
    fun getRelativePath(folder: FolderResource): String {
        val path = mutableListOf<FolderResource>()
        var current: FolderResource? = folder

        while (current != null) {
            path.add(current)
            current = current.parent
        }

        return path.reversed().joinToString("/", prefix = "/", postfix = "/") { it.title.value }
    }

}

/**
 * Перечисление, определяющее типы ресурсов для отображения в менеджере
 */
enum class ResourceType {
    ALL,        // Все файлы
    VIDEO,      // Только видео файлы
    AUDIO,      // Только аудио файлы
    IMAGE,      // Только изображения
    TEXT,       // Только текстовые вставки
    EFFECTS     // Только эффекты
}

