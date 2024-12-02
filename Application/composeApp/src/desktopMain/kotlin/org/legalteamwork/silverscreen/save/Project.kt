package org.legalteamwork.silverscreen.save

import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.re.VideoEditor.VideoTrack.ResourceOnTrack
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.VideoResource

@Serializable
class ProjectData {
    var bitrate: Int
    var fps: Double
    var resolution: Resolution

    var resources: FolderResource
    var resourcesOnTrack: List<ResourceOnTrack>
    var trackResources: List<VideoResource>

    init {
        // project initialization
        bitrate = 6000
        fps = 30.0
        resolution = Resolution.default
        resources = FolderResource.defaultRoot
        resourcesOnTrack = listOf()
        trackResources = listOf()
    }

    fun sync() {
        resources = ResourceManager.currentFolder.value
        while (resources.parent != null)
            resources = resources.parent!!
        resourcesOnTrack = VideoEditor.getResourcesOnTrack()
        trackResources = VideoEditor.getVideoResources()
    }

    fun restore() {
        ResourceManager.currentFolder.value = resources
        ResourceManager.currentFolder.value.assignParents()

        VideoEditor.restore(resourcesOnTrack, trackResources)
    }
}

object Project : SaveManager<ProjectData>(ProjectData::class) {
    init {
        value = ProjectData()
    }

    fun reset() {
        value = ProjectData()
        value!!.restore()
    }

    override fun save() {
        value!!.sync()
        super.save()
    }
    override fun load(): Boolean {
        val result = super.load()
        if (result) value!!.restore()
        return result
    }

    fun autosave() {
        if (EditorSettings.get().autosaveEnabled.value)
            save("autosave.json")
    }
    fun autoload() {
        if (EditorSettings.get().autosaveEnabled.value)
            load("autosave.json")
        setPath(null)
    }
}