package org.legalteamwork.silverscreen.rm

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import java.io.File

@Serializable
private data class Project(
    val resources: List<VideoResource>,
    val tracks: List<VideoEditor.Track>
)

object SaveManager {
    private const val autosavePath: String = "data.json"

    fun save(jsonPath: String = autosavePath) : String {
        val videoOnly = ResourceManager.videoResources.value.resources.filterIsInstance<VideoResource>()
        val tracks = VideoEditor.getTracks()

        val project = Project(videoOnly, tracks)
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

        ResourceManager.videoResources.value.resources.clear()
        ResourceManager.videoResources.value.resources.addAll(data.resources)
        VideoEditor.restore(data.tracks)
    }
}