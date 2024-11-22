package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.Resource
import org.legalteamwork.silverscreen.rm.resource.SimpleResource
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import org.legalteamwork.silverscreen.rm.window.EffectsMainWindow
import org.legalteamwork.silverscreen.rm.window.ErrorMainWindow
import org.legalteamwork.silverscreen.rm.window.PresetsMainWindow
import org.legalteamwork.silverscreen.rm.window.TemplatesMainWindow
import org.legalteamwork.silverscreen.rm.window.source.SourcesMainWindow
import java.awt.FileDialog
import java.awt.Frame
import java.awt.datatransfer.DataFlavor
import java.io.File
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.ResourceManagerTheme

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

    // Fields:
    private val buttonId = mutableStateOf(Dimens.INIT_ID)
    private val buttons = listOf(
        MenuButton(Dimens.SOURCES_ID, Strings.SOURCES),
        MenuButton(Dimens.EFFECTS_ID, Strings.EFFECTS),
        MenuButton(Dimens.PRESETS_ID, Strings.PRESETS),
        MenuButton(Dimens.TEMPLATES_ID, Strings.TEMPLATES),
    )
    val rootFolder: FolderResource =
        FolderResource(mutableStateOf("root"), parent = null, resources = mutableStateListOf())
    val videoResources: MutableState<FolderResource> = mutableStateOf(rootFolder)
    val activeResource: MutableState<Resource?> = mutableStateOf(null)

    //Режимы отображения: список
    val isListView = mutableStateOf(false)

    fun toggleViewMode() {
        isListView.value = !isListView.value
    }

    @Composable
    fun compose() {
        BoxWithConstraints(
            modifier = Modifier.background(
                color = ResourceManagerTheme.RESOURCE_MANAGER_BACKGROUND_COLOR,
                shape = RoundedCornerShape(8.dp),
            ).fillMaxSize()
        ) {
            val adaptiveMenuWidth = max(min(maxWidth * 0.3f, Dimens.MENU_MAX_WIDTH), Dimens.MENU_MIN_WIDTH)
            val adaptiveMainWindowWidth = maxWidth - adaptiveMenuWidth

            Row {
                Menu(adaptiveMenuWidth)
                MainWindow(adaptiveMainWindowWidth)
            }
        }
    }

    /**
     * Триггерит вызов окна с выбором ресурса с последующей обработкой и созранением в [videoResources]
     */
    fun addSourceTriggerActivity() {
        logger.info { "Triggering file picker function openFileDialog for video resources" }
        val loadFiles = openFileDialog(null, "File Picker", listOf(".mp4"))

        if (loadFiles.isEmpty()) {
            logger.warn { "No files selected in file picker" }
        }
        else {
            logger.info { "Files selected: ${loadFiles.joinToString { it.path }}" }
            for (loadFile in loadFiles) {
                val resource = VideoResource(loadFile.path, videoResources.value)
                addSource(resource)
                logger.info { "Added video resource: ${resource.title.value}" }
            }
        }
    }

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

    /**
     * Добавление ресурса
     */
    fun addSource(resource: Resource) {
        logger.info { "Adding resource: ${resource.title.value}" }
        videoResources.value.resources.add(resource)
        logger.info { "Resource ${resource.title.value} added successfully" }
    }

    /**
     * Удаление ресурса
     *
     * @param[resource] дата ресуса
     */
    fun removeSource(resource: Resource) {
        logger.info { "Removing resource: ${resource.title.value}" }
        if ((videoResources.value.resources.remove(resource))) {
            logger.info { "Resource ${resource.title.value} removed successfully" }
        }
        else {
            logger.warn { "Failed to remove resource: ${resource.title.value}" }
        }
    }

    /**
     * Changes current showing folder to the parent one if it is possible
     */
    fun onFolderUp() {
        val parent = videoResources.value.parent

        if (videoResources.value != rootFolder && parent != null) {
            videoResources.component2().invoke(parent)
            logger.info { "Navigated up to folder: ${parent.title.value}" }
        }
        else {
            logger.warn { "Cannot navigate up: already at the root folder or no parent." }
        }
    }

    //Private methods:

    /**
     * Отображение бокового меню
     */
    @Composable
    private fun Menu(menuWidth: Dp) {
        Box(
            modifier = Modifier.background(color = ResourceManagerTheme.MENU_COLOR, RoundedCornerShape(8.dp)).width(menuWidth)
                .fillMaxHeight()
        ) {
            ButtonList()
        }
    }

    /**
     * Отображение конкретно кнопок
     */
    @Composable
    private fun ButtonList() {
        Column(modifier = Modifier.padding(5.dp)) {
            for (button in buttons) {
                MenuButton(button)
            }
        }
    }

    /**
     * Отображение конкретной кнопки
     *
     * @param[button] инфомация о кнопке
     */
    @Composable
    private fun MenuButton(button: MenuButton) {
        var chosenButton by remember { buttonId }
        val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = ResourceManagerTheme.MENU_BUTTONS_BACKGROUND_COLOR,
            contentColor = ResourceManagerTheme.MENU_BUTTONS_CONTENT_COLOR,
            disabledBackgroundColor = ResourceManagerTheme.MENU_BUTTONS_DISABLED_BACKGROUND_COLOR,
            disabledContentColor = ResourceManagerTheme.MENU_BUTTONS_DISABLED_CONTENT_COLOR,
        )

        Button(
            onClick = { chosenButton = button.id },
            modifier = Modifier.fillMaxWidth().height(Dimens.MENU_BUTTON_HEIGHT),
            colors = buttonColors,
            elevation = null,
            border = null,
            enabled = chosenButton != button.id,
        ) {
            Text(
                text = button.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                fontFamily = ResourceManagerTheme.MENU_FONT_FAMILY,
            )
        }
    }

    /**
     * Отображенрие основного окна, которое содержит превью ресурсов,
     * с которыми можно взаимодействовать
     */
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
    @Composable
    private fun MainWindow(windowWidth: Dp) {
        val id by remember { buttonId }

        Box(modifier = Modifier.width(windowWidth).fillMaxHeight().dragAndDropTarget(shouldStartDragAndDrop = { event ->
            event.awtTransferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
        }, target = remember {
            object : DragAndDropTarget {

                override fun onDrop(event: DragAndDropEvent): Boolean {

                    logger.info { "Files started dropping in the window" }
                    val files =
                        (event.awtTransferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>).filter { it.extension == "mp4" }

                    if (files.isEmpty()) {
                        logger.warn { "No MP4 files were dropped." }
                    }
                    else {
                        logger.info { "Dropped MP4 files: ${files.joinToString { it.name }}" }
                    }

                    for (file in files) {
                        val resource = VideoResource(file.path, videoResources.value)
                        addSource(resource)

                        logger.info { "Successfully added resource: ${resource.title.value}" }
                    }

                    return true
                }
            }
        })) {
            logger.info { "Displaying window with id: $id" }
            when (id) {
                Dimens.SOURCES_ID -> SourcesMainWindow()
                Dimens.EFFECTS_ID -> EffectsMainWindow()
                Dimens.PRESETS_ID -> PresetsMainWindow()
                Dimens.TEMPLATES_ID -> TemplatesMainWindow()
                else -> ErrorMainWindow()
            }
        }
    }
}
