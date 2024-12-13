package org.legalteamwork.silverscreen.re

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import org.legalteamwork.silverscreen.rm.window.effects.VideoEffect

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ResourceOnTrack::class)
class ResourceOnTrackSerializer : KSerializer<ResourceOnTrack> {
    private val logger = KotlinLogging.logger { }

    override fun serialize(
        encoder: Encoder,
        value: ResourceOnTrack,
    ) {
        logger.info { "Serializing video resource" }
        encoder.encodeInt(value.id)
        encoder.encodeInt(value.position)
        encoder.encodeInt(value.framesCount)
        encoder.encodeInt(value.framesSkip)
        SnapshotVideoEffectListSerializer.serialize(encoder, value.effects)
    }

    override fun deserialize(decoder: Decoder): ResourceOnTrack {
        logger.info { "Deserializing video resource" }
        val id = decoder.decodeInt()
        val position = decoder.decodeInt()
        val framesCountDefault = decoder.decodeInt()
        val framesSkip = decoder.decodeInt()
        val effects = SnapshotVideoEffectListSerializer.deserialize(decoder)

        return ResourceOnTrack(null, id, position, framesCountDefault, framesSkip, effects)
    }

    object SnapshotVideoEffectListSerializer : KSerializer<SnapshotStateList<VideoEffect>> {
        private val delegateSerializer = serializer<List<VideoEffect>>()

        @OptIn(ExperimentalSerializationApi::class)
        override val descriptor = SerialDescriptor(this.javaClass.name, delegateSerializer.descriptor)

        override fun serialize(encoder: Encoder, value: SnapshotStateList<VideoEffect>) {
            encoder.encodeSerializableValue(delegateSerializer, value.toList())
        }

        override fun deserialize(decoder: Decoder): SnapshotStateList<VideoEffect> {
            val value = decoder.decodeSerializableValue(delegateSerializer)
            return mutableStateListOf<VideoEffect>().apply { addAll(value) }
        }
    }
}