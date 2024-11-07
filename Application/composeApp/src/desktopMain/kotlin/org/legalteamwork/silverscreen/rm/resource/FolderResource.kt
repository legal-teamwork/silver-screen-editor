package org.legalteamwork.silverscreen.rm.resource

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

class FolderResource(
    override val title: MutableState<String>,
    val resources: SnapshotStateList<Resource> = mutableStateListOf(),
) : Resource {
    override val previewPath: String = "src/desktopMain/resources/svg/folder.svg"
    override val properties: ResourceProperties
        get() = ResourceProperties(
            listOf(
                ResourceProperty("Description", "Resource type", "Folder"),
                ResourceProperty("Description", "Title", title.value),
                ResourceProperty("Description", "Title", title.value),
            )
        )
    val folderResources: List<Resource>
        get() = resources.filterIsInstance<FolderResource>()
    val dataResources: List<Resource>
        get() = resources.filterNot { it is FolderResource }

    override fun clone(): Resource {
        return FolderResource(mutableStateOf("${title.value} (clone)"), resources.map(Resource::clone).toMutableStateList())
    }
}
