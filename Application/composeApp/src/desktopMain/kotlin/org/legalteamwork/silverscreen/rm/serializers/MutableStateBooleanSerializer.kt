package org.legalteamwork.silverscreen.rm.serializers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object MutableStateBooleanSerializer : KSerializer<MutableState<Boolean>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(this.javaClass.name, PrimitiveKind.BOOLEAN)

    override fun serialize(encoder: Encoder, value: MutableState<Boolean>) {
        encoder.encodeBoolean(value.value)
    }

    override fun deserialize(decoder: Decoder): MutableState<Boolean> {
        val value = decoder.decodeBoolean()

        return mutableStateOf(value)
    }
}