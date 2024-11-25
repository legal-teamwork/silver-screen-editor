package org.legalteamwork.silverscreen.save

import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.VideoEditor.VideoTrack.ResourceOnTrack
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.VideoResource

@Serializable
class ProjectData {
    var bitrate: Int
    var fps: Double
    var resolution: String

    var resources: FolderResource
    var resourcesOnTrack: List<ResourceOnTrack>
    var trackResources: List<VideoResource>

    init {
        // project initialization
        bitrate = 6000
        fps = 30.0
        resolution = "1920x1080 (Full HD; 1080p; 16:9)"
        resources = FolderResource.defaultRoot
        resourcesOnTrack = listOf()
        trackResources = listOf()
    }

    fun sync() {
        resources = ResourceManager.videoResources.value
        while (resources.parent != null)
            resources = resources.parent!!
        resourcesOnTrack = VideoEditor.getResourcesOnTrack()
        trackResources = VideoEditor.getVideoResources()
    }

    fun restore() {
        ResourceManager.videoResources.value = resources
        ResourceManager.videoResources.value.assignParents()

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