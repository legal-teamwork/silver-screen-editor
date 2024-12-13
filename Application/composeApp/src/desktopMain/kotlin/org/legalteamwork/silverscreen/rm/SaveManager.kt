package org.legalteamwork.silverscreen.rm

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.legalteamwork.silverscreen.re.ResourceOnTrack
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import org.legalteamwork.silverscreen.re.VideoEditor
import org.legalteamwork.silverscreen.re.VideoTrack
import java.io.File

@Serializable
private data class Project(
    val resources: List<VideoResource>,
    //val tracks: List<VideoEditor.VideoTrack>
    val resourcesOnTrack: List<ResourceOnTrack>,
    val trackResources: List<VideoResource>
)

object SaveManager {
    private const val autosavePath: String = "data.json"

    fun save(jsonPath: String = autosavePath) : String {
        val videoOnly = ResourceManager.currentFolder.value.resources.filterIsInstance<VideoResource>()
        //val tracks = VideoEditor.getTracks()
        val resourcesOnTrack = VideoEditor.getResourcesOnTrack()
        val trackResources = VideoEditor.getVideoResources()

        val project = Project(videoOnly, resourcesOnTrack, trackResources)
        val str = Json.encodeToString(project)
        val jsonFile = File(jsonPath)
        jsonFile.writeText(str)
        println(str)
        return str
    }

    fun load(jsonPath: String = autosavePath) {
        val jsonFile = File(jsonPath)
        if (!jsonFile.exists()) return
        val str = jsonFile.readText()
        val data = Json.decodeFromString<Project>(str)

        ResourceManager.currentFolder.value.resources.clear()
        ResourceManager.currentFolder.value.resources.addAll(data.resources)
        ResourceManager.currentFolder.value.resources.addAll(data.resources)

        VideoEditor.restore(data.resourcesOnTrack, data.trackResources)
    }
}