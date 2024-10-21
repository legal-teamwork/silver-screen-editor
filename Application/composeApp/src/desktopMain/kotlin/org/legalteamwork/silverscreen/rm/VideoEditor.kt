package org.legalteamwork.silverscreen.rm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.rm.window.*

/**
 * Базовый класс для панели редактирования видео.
 */
object VideoEditor {
    // Constants
    private val TRACK_MAX_HEIGHT = 500.dp
    private val TRACK_MIN_WIDTH = 30.dp
    private val MENU_FONT_FAMILY = FontFamily.Default

    @Composable
    fun compose() {
        BoxWithConstraints(
            modifier = Modifier.background(
                color = Color(0xFF444444),
                shape = RoundedCornerShape(8.dp),
            ).fillMaxSize()
        ) {
            val adaptiveTrackHeight = max(min(maxHeight * 0.4f, TRACK_MAX_HEIGHT), TRACK_MIN_WIDTH)

            Column (
                modifier = Modifier
                    .padding(vertical = maxHeight * 0.05f),
                verticalArrangement = Arrangement.spacedBy(maxHeight * 0.05f)
            ){
                track(adaptiveTrackHeight)
            }
        }
    }

    @Composable
    private fun track(trackHeight: Dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .background(color=Color.Gray)
        )
    }
}

