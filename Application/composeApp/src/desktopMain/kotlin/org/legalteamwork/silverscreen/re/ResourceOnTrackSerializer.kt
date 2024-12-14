package org.legalteamwork.silverscreen.re

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ResourceOnTrack::class)
class ResourceOnTrackSerializer : KSerializer<ResourceOnTrack> {
    private val logger = KotlinLogging.logger { }

    override fun serialize(
        encoder: Encoder,
        value: ResourceOnTrack,
    ) {
        logger.info { "Serializing video resource" }
        encoder.encodeSerializableValue(IntArraySerializer(), intArrayOf(value.id, value.position, value.framesCount, value.framesSkip))
    }

    override fun deserialize(decoder: Decoder): ResourceOnTrack {
        logger.info { "Deserializing video resource" }
        val array = decoder.decodeSerializableValue(IntArraySerializer())
        return ResourceOnTrack(null, array[0], array[1], array[2], array[3])
    }
}