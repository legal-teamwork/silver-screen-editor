package org.legalteamwork.silverscreen.save

import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.VideoEditor
import org.legalteamwork.silverscreen.rm.VideoEditor.VideoTrack.ResourceOnTrack
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.VideoResource

@Serializable
class Project {
    lateinit var resources: FolderResource
    lateinit var resourcesOnTrack: List<ResourceOnTrack>
    lateinit var trackResources: List<VideoResource>

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

object ProjectSaveManager : SaveManager<Project>(Project::class) {
    init {
        value = Project()
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
        if (EditorSettingsSaveManager.get().autosaveEnabled.value)
            save("autosave.json")
    }
    fun autoload() {
        if (EditorSettingsSaveManager.get().autosaveEnabled.value)
            load("autosave.json")
        setPath(null)
    }
}