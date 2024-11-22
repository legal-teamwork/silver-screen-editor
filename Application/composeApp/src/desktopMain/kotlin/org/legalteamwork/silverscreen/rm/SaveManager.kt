package org.legalteamwork.silverscreen.rm

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import java.io.File

private val logger = KotlinLogging.logger {  }

@Serializable
private data class Project(
    val resources: List<VideoResource>,
    //val tracks: List<VideoEditor.VideoTrack>
    val resourcesOnTrack: List<VideoEditor.VideoTrack.ResourceOnTrack>,
    val trackResources: List<VideoResource>
)

object SaveManager {
    private const val autosavePath: String = "data.json"

    fun save(jsonPath: String = autosavePath) : String {
        logger.info { "Saving project..." }
        val videoOnly = ResourceManager.videoResources.value.resources.filterIsInstance<VideoResource>()
        //val tracks = VideoEditor.getTracks()
        val resourcesOnTrack = VideoEditor.getResourcesOnTrack()
        val trackResources = VideoEditor.getVideoResources()

        val project = Project(videoOnly, resourcesOnTrack, trackResources)
        val str = Json.encodeToString(project)
        val jsonFile = File(jsonPath)
        jsonFile.writeText(str)
        println(str)
        logger.info { "Saved project successfully" }
        return str
    }

    fun load(jsonPath: String = autosavePath) {
        logger.info { "Loading project..." }
        val jsonFile = File(jsonPath)
        if (!jsonFile.exists()) return
        val str = jsonFile.readText()
        val data = Json.decodeFromString<Project>(str)

        ResourceManager.videoResources.value.resources.clear()
        ResourceManager.videoResources.value.resources.addAll(data.resources)

        VideoEditor.restore(data.resourcesOnTrack, data.trackResources)
        logger.info { "Loaded project successfully" }
    }
}