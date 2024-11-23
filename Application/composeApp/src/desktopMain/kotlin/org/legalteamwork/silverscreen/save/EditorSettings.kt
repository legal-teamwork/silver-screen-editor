package org.legalteamwork.silverscreen.save

import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import org.legalteamwork.silverscreen.rm.serializers.MutableStateBooleanSerializer


@Serializable
class EditorSettings {
    @Serializable(with = MutableStateBooleanSerializer::class)
    val autosaveEnabled = mutableStateOf(true)
}

object EditorSettingsSaveManager : SaveManager<EditorSettings>(EditorSettings::class) {
    init {
        value = EditorSettings()
        savePath = "settings.json"
    }
}