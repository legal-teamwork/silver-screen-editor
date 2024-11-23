package org.legalteamwork.silverscreen.rm.serializers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object MutableStateStringSerializer : KSerializer<MutableState<String>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(this.javaClass.name, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: MutableState<String>) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): MutableState<String> {
        val value = decoder.decodeString()

        return mutableStateOf(value)
    }
}