package org.legalteamwork.silverscreen.save

import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.rm.serializers.MutableStateBooleanSerializer


@Serializable
class EditorSettingsData {
    @Serializable(with = MutableStateBooleanSerializer::class)
    val autosaveEnabled = mutableStateOf(true)
}

object EditorSettings : SaveManager<EditorSettingsData>(EditorSettingsData::class) {
    init {
        value = EditorSettingsData()
        savePath = "settings.json"
    }
}