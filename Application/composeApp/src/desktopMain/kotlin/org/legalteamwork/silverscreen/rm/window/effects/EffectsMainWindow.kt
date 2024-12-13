package org.legalteamwork.silverscreen.rm.window.effects

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.EffectsTabTheme
import org.legalteamwork.silverscreen.resources.SourcesMenuButtonTheme
import java.awt.datatransfer.StringSelection

@Composable
fun AppScope.EffectsMainWindow() {
    LazyColumn(Modifier.padding(10.dp)) {
        items(effectsManager.videoEffects) {
            EffectItem(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EffectItem(videoEffect: VideoEffect) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(EffectsTabTheme.EFFECT_BACKGROUND, shape = RoundedCornerShape(5.dp))
            .dragAndDropSource(
                drawDragDecoration = { }
            ) {
                detectDragGestures(
                    onDragStart = { offset ->
                        startTransfer(
                            DragAndDropTransferData(
                                transferable = DragAndDropTransferable(
                                    EffectTransferable(videoEffect)
                                ),
                                supportedActions = listOf(
                                    DragAndDropTransferAction.Move,
                                ),
                                onTransferCompleted = { action ->
                                    println("Action at the source: $action")
                                }
                            )
                        )
                    },
                    onDrag = { _, _ -> }
                )
            },
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
