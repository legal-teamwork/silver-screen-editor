package org.legalteamwork.silverscreen.toolbar.components;

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.foundation.Image
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.legalteamwork.silverscreen.resources.EditingPanelTheme

@Composable
fun leftEditingTools(
    modifier: Modifier = Modifier,
    onCutClick: () -> Unit,
    onVolumeChange: (Float) -> Unit = {} // Placeholder for volume change
) {
    var volume by remember { mutableStateOf(0.5f) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCutClick) {
            Image(
                painter = painterResource("toolbar_buttons/content_cut.svg"),
                contentDescription = "Cut"
            )
        }
        /** Слайдер регулировки громкости
        Slider(
            value = volume,
            onValueChange = {
                volume = it
                onVolumeChange(it)
            },
            modifier = Modifier.widthIn(min = 50.dp, max = 150.dp),
            colors = SliderDefaults.colors(
                thumbColor = EditingPanelTheme.SLIDER_COLOR,
                activeTrackColor = EditingPanelTheme.SLIDER_COLOR
            )
        ) */
    }
}
