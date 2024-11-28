package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProviderAtPosition
import org.legalteamwork.silverscreen.resources.CopyToWindowTheme
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.Resource

private fun collectPossibleFolders(
    folder: FolderResource = ResourceManager.rootFolder
): List<Pair<FolderResource, String>> = folder.folderResources
    .map { it as FolderResource }
    .flatMap { collectPossibleFolders(it).map { (innerFolder, innerPath) -> innerFolder to "${folder.title.value}/$innerPath" } }
    .plus(folder to "${folder.title.value}/")

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CopyToWindow(
    contextWindowData: ContextWindowData,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit
) {
    val resource: Resource = ResourceManager.activeResource.value ?: return
    val position = contextWindowData.position
    val shape = RoundedCornerShape(5.dp)
    val possibleFolders: List<Pair<FolderResource, String>> = collectPossibleFolders()

    Popup(
        popupPositionProvider = PopupPositionProviderAtPosition(position, true, Offset.Zero, Alignment.BottomEnd, 4),
        onDismissRequest = onContextWindowClose,
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .width(Dimens.WINDOW_WIDTH)
                .heightIn(min = 0.dp, max = Dimens.WINDOW_HEIGHT)
                .wrapContentSize()
                .shadow(5.dp, shape = shape)
                .background(CopyToWindowTheme.BACKGROUND_COLOR, shape = shape)
                .border(1.dp, CopyToWindowTheme.BORDER_COLOR, shape = shape)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                LazyColumn {
                    items(possibleFolders, { it }) { (folder, folderPath) ->
                        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable {
                            folder.resources.add(resource.clone())
                            onContextWindowClose()
                        }) {
                            Text(
                                text = "/$folderPath", modifier = Modifier.padding(10.dp, 2.dp), color = CopyToWindowTheme.TEXT_COLOR
                            )
                        }
                    }
                }
            }
        }
    }
}
