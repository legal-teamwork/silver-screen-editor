package org.legalteamwork.silverscreen.rm.window.effects

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.EffectsTabTheme
import org.legalteamwork.silverscreen.resources.SourcesMenuButtonTheme

@Composable
fun AppScope.EffectsMainWindow() {
    LazyColumn(Modifier.padding(10.dp)) {
        items(effectsManager.videoEffects) {
            EffectItem(it)
        }
    }
}

@Composable
fun EffectItem(videoEffect: VideoEffect) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(EffectsTabTheme.EFFECT_BACKGROUND, shape = RoundedCornerShape(5.dp)),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(videoEffect.previewPath),
            contentDescription = videoEffect.title,
            modifier = Modifier.fillMaxHeight().padding(5.dp),
            contentScale = ContentScale.Fit
        )

        // Название файла
        Text(
            text = videoEffect.title,
            modifier = Modifier.weight(1f),
            color = SourcesMenuButtonTheme.PATH_WINDOW_TEXT_COLOR,
        )
    }
}
