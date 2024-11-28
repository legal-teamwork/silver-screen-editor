package org.legalteamwork.silverscreen.rm.serializers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import org.legalteamwork.silverscreen.rm.resource.Resource

object SnapshotResourceListSerializer : KSerializer<SnapshotStateList<Resource>> {
    private val delegateSerializer = serializer<List<Resource>>()

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor = SerialDescriptor(this.javaClass.name, delegateSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: SnapshotStateList<Resource>) {
        encoder.encodeSerializableValue(delegateSerializer, value.toList())
    }

    override fun deserialize(decoder: Decoder): SnapshotStateList<Resource> {
        val value = decoder.decodeSerializableValue(delegateSerializer)
        return mutableStateListOf<Resource>().apply { addAll(value) }
    }
}