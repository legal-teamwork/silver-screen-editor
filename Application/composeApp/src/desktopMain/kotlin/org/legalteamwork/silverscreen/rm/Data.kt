package org.legalteamwork.silverscreen.rm

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.legalteamwork.silverscreen.rm.resource.VideoResource
import org.legalteamwork.silverscreen.rm.ResourceManager
import org.legalteamwork.silverscreen.rm.resource.ResourceFrame
import java.io.File

object Data {

    fun generateVideoResourceData(jsonPath: String = "data.json"): List<VideoResource> {
        val jsonFile = File(jsonPath)
        if (!jsonFile.exists()) return listOf()
        val str = jsonFile.readText()

        val data = Json.decodeFromString<List<VideoResource>>(str)
        return data
    }

    fun saveVideoResourceData(jsonPath: String = "data.json") : String {
        val videoOnly = mutableListOf<VideoResource>()
        for (resource in ResourceManager.videoResources) {
            if (resource is VideoResource) {
                println("plus one")
                videoOnly.addLast(resource)
            }
        }
        val str = Json.encodeToString(videoOnly)
        val jsonFile = File(jsonPath)
        jsonFile.writeText(str)
        println(str)
        return str
    }

    fun generateAllResources(jsonPath: String = "data.json") {
        val videoResources = generateVideoResourceData(jsonPath)
        ResourceManager.videoResources.clear()
        ResourceManager.videoResources.addAll(videoResources)
    }

}