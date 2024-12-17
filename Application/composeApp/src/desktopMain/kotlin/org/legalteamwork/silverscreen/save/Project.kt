package org.legalteamwork.silverscreen.save

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.re.ResourceOnTrack
import org.legalteamwork.silverscreen.rm.resource.FolderResource
import org.legalteamwork.silverscreen.rm.resource.VideoResource

private val logger = KotlinLogging.logger {}

@Serializable
class ProjectData {
    var bitrate: Int = 6000
    var fps: Double = 30.0
    var resolution: Int = Resolution.default

    var resources: FolderResource = FolderResource.createRoot()
    var resourcesOnTrack: List<ResourceOnTrack> = listOf()
    var trackResources: List<VideoResource> = listOf()

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
        logger.info { "Project restore finished" }
    }
}

object Project : SaveManager<ProjectData>(ProjectData::class) {
    init {
        value = ProjectData()
    }

    val fps
        get() = get{fps}

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