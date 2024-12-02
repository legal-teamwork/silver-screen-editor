package org.legalteamwork.silverscreen.save

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {  }

@OptIn(InternalSerializationApi::class)
open class SaveManager<T: Any>(private val type: KClass<T>) {
    protected var value: T? = null
    protected var savePath: String? = null

    val hasPath: Boolean
        get() = Project.savePath != null
    fun setPath(s: String?) {
        savePath = s
    }

    fun get(): T = value!!
    fun change(action: T.() -> Unit) {
        value!!.apply(action)
    }
    fun<U> get(action: T.() -> U) = value!!.run(action)

    open fun save() {
        if (savePath == null) {
            logger.error { "Saving ${type.simpleName} failed: no save path specified " }
            return
        }
        logger.info { "Saving ${type.simpleName} to $savePath.." }
        val str = Json.encodeToString(type.serializer(), get())
        val jsonFile = File(savePath!!)
        jsonFile.writeText(str)
    }

    open fun load(): Boolean {
        if (savePath == null) {
            logger.error { "Loading ${type.simpleName} failed: no save path specified " }
            return false
        }
        logger.info { "Loading ${type.simpleName} from $savePath..." }
        val jsonFile = File(savePath!!)
        if (!jsonFile.exists()) {
            logger.error { "Loading failed: file does not exist" }
            return false
        }
        val str = jsonFile.readText()
        try {
            value = Json.decodeFromString(type.serializer(), str)
            logger.info { "Loaded successfully" }
            return true
        } catch (_: SerializationException) {
            logger.error { "Loading failed: incorrect file syntax" }
            return false
        }
    }

    fun save(path: String) {
        setPath(path)
        save()
    }

    fun load(path: String): Boolean {
        setPath(path)
        return load()
    }
}