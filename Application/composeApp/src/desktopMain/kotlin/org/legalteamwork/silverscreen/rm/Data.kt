package org.legalteamwork.silverscreen.rm

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.ResourceFrame
import java.io.File

object Data {

    val jsonPath : String = "data.json"
    val jsonFile = File(jsonPath)

    fun generateVideoResourceData() : List<VideoResource> {
        val str = jsonFile.readText()
        return Json.decodeFromString<List<VideoResource>>(str)
    }

    fun saveVideoResourceData() : String {
        val videoOnly = mutableListOf<VideoResource>()
        for (resource in ResourceManager.videoResources) {
            if (resource is VideoResource) {
                println("plus one")
                videoOnly.addLast(resource)
            }
        }
        val str = Json.encodeToString(videoOnly)
        jsonFile.writeText(str)
        println(str)
        return str
    }

    fun generateAllResources() {
        val videoResources = generateVideoResourceData()
        ResourceManager.videoResources.clear()
        ResourceManager.videoResources.addAll(videoResources)
    }

}