package org.legalteamwork.silverscreen.re

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.edit.MoveResourceOnTrackCommand
import org.legalteamwork.silverscreen.re.VideoEditor.VideoTrack
import org.legalteamwork.silverscreen.re.VideoEditor.VideoTrack.highlightedResources
import org.legalteamwork.silverscreen.re.VideoEditor.VideoTrack.resourcesOnTrack
import org.legalteamwork.silverscreen.re.VideoEditor.VideoTrack.videoResources
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt


fun VideoEditor.highlightResource(id: Int) : Boolean {
    println(highlightedResources)
    if (highlightedResources.contains(id)) {
        highlightedResources.remove(id)
        return false
    }
    else {
        highlightedResources.add(id)
        return true
    }
}

fun VideoEditor.getHighlightedResources() = highlightedResources



