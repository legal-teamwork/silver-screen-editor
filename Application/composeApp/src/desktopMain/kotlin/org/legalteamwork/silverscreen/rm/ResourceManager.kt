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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
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

    // Constants
    private const val INIT_ID = 1
    private const val SOURCES_ID = 1
    private const val EFFECTS_ID = 2
    private const val PRESETS_ID = 3
    private const val TEMPLATES_ID = 4
    private val MENU_MIN_WIDTH = 150.dp
    private val MENU_MAX_WIDTH = 275.dp
    private val MENU_BUTTON_HEIGHT = 35.dp
    private val MENU_FONT_FAMILY = FontFamily.Default

    // Fields:
    private val buttonId = mutableStateOf(INIT_ID)
    private val buttons = listOf(
        MenuButton(SOURCES_ID, "Sources"),
        MenuButton(EFFECTS_ID, "Effects"),
        MenuButton(PRESETS_ID, "Presets"),
        MenuButton(TEMPLATES_ID, "Templates"),
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
                color = Color(0xFF444444),
                shape = RoundedCornerShape(8.dp),
            ).fillMaxSize()
        ) {
            val adaptiveMenuWidth = max(min(maxWidth * 0.3f, MENU_MAX_WIDTH), MENU_MIN_WIDTH)
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
        videoResources.value.resources.remove(resource)
        if (!videoResources.value.resources.remove(resource)) {
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
            modifier = Modifier.background(color = Color(0xFF3A3A3A), RoundedCornerShape(8.dp)).width(menuWidth)
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
            backgroundColor = Color(0xFF3A3A3A),
            contentColor = Color.White,
            disabledBackgroundColor = Color(0xFF222222),
            disabledContentColor = Color.White,
        )

        Button(
            onClick = { chosenButton = button.id },
            modifier = Modifier.fillMaxWidth().height(MENU_BUTTON_HEIGHT),
            colors = buttonColors,
            elevation = null,
            border = null,
            enabled = chosenButton != button.id,
        ) {
            Text(
                text = button.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                fontFamily = MENU_FONT_FAMILY,
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

                    val files =
                        (event.awtTransferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>).filter { it.extension == "mp4" }

                    for (file in files) {
                        val resource = VideoResource(file.path, videoResources.value)
                        addSource(resource)
                    }

                    return true
                }
            }
        })) {
            when (id) {
                SOURCES_ID -> SourcesMainWindow()
                EFFECTS_ID -> EffectsMainWindow()
                PRESETS_ID -> PresetsMainWindow()
                TEMPLATES_ID -> TemplatesMainWindow()
                else -> ErrorMainWindow()
            }
        }
    }
}
