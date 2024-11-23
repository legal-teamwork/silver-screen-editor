package org.legalteamwork.silverscreen.rm.resource

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.serializers.MutableStateStringSerializer
import org.legalteamwork.silverscreen.rm.serializers.SnapshotResourceListSerializer

@Serializable
class FolderResource(
    @Serializable(with = MutableStateStringSerializer::class)
    override val title: MutableState<String>,
    @Transient
    override var parent: FolderResource? = null,
    @Serializable(with = SnapshotResourceListSerializer::class)
    val resources: SnapshotStateList<Resource> = mutableStateListOf(),
) : Resource {
    override val previewPath: String = "src/desktopMain/resources/svg/folder.svg"
    override val properties: ResourceProperties
        get() = ResourceProperties(
            listOf(
                ResourceProperty("Description", "Resource type", "Folder"),
                ResourceProperty("Description", "Title", title.value),
            )
        )
    val folderResources: List<Resource>
        get() = resources.filterIsInstance<FolderResource>()
    val dataResources: List<Resource>
        get() = resources.filterNot { it is FolderResource }

    fun addResource(resource: Resource) {
        resources.add(resource)
        resource.parent = this
    }

    override fun clone(): Resource {
        return FolderResource(
            mutableStateOf("${title.value} (clone)"),
            parent,
            resources.map(Resource::clone).toMutableStateList()
        )
    }

    override fun action() {
        ResourceManager.videoResources.component2().invoke(this)
    }

    fun assignParents() {
        resources.forEach { child ->
            child.parent = this
            if (child is FolderResource)
                child.assignParents()
        }
    }
}
