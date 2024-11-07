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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProviderAtPosition
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.FolderResource

private fun collectPossibleFolders(
    folder: FolderResource = ResourceManager.rootFolder, except: FolderResource? = null
): List<Pair<FolderResource, String>> = if (folder == except) {
    emptyList()
} else {
    folder.folderResources
        .map { it as FolderResource }
        .flatMap {
            collectPossibleFolders(
                it,
                except
            ).map { (folder, innerPath) -> folder to "${folder.title.value}/$innerPath" }
        }
        .plus(folder to "${folder.title.value}/")
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoveToWindow(
    contextWindowData: ContextWindowData,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit,
) {
    val resource = contextWindowData.resource
    val position = contextWindowData.position
    val width = 250.dp
    val height = 400.dp
    val shape = RoundedCornerShape(5.dp)
    val possibleFolders: List<Pair<FolderResource, String>> = if (resource is FolderResource) {
        collectPossibleFolders(except = resource)
    } else {
        collectPossibleFolders()
    }

    Popup(
        popupPositionProvider = PopupPositionProviderAtPosition(position, true, Offset.Zero, Alignment.BottomEnd, 4),
        onDismissRequest = onContextWindowClose,
    ) {
        Box(
            modifier = Modifier.wrapContentSize().width(width).heightIn(min = 0.dp, max = height).wrapContentSize()
                .shadow(5.dp, shape = shape).background(Color.DarkGray, shape = shape)
                .border(1.dp, Color.LightGray, shape = shape)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                LazyColumn {
                    items(possibleFolders, { it }) { (folder, folderPath) ->
                        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable {
                            resource.parent?.resources?.remove(resource)
                            folder.addResource(resource)
                            onContextWindowClose()
                        }) {
                            Text(
                                text = folderPath, modifier = Modifier.padding(10.dp, 2.dp), color = Color.White
                            )
                        }
                    }

//                    item {
//                        Divider(
//                            modifier = Modifier.fillMaxWidth(),
//                            color = Color.Gray,
//                            thickness = 1.dp,
//                        )
//                    }
//
//                    item {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .wrapContentHeight()
//                                .clickable {
//                                    // TODO
//                                }
//                        ) {
//                            Text(
//                                text = "New folder...",
//                                modifier = Modifier.padding(10.dp, 2.dp),
//                                color = Color.White
//                            )
//                        }
//                    }
                }
            }
        }
    }
}
