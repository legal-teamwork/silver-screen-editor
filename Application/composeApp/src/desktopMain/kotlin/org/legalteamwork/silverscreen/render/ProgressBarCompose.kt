package org.legalteamwork.silverscreen.render

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import org.legalteamwork.silverscreen.resources.AppTheme
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.resources.EditingPanelTheme
import kotlin.math.roundToInt

@Composable
fun ExportProgressDialog(isExporting: MutableState<Boolean>, progress: MutableState<Float>) {
    if (isExporting.value) {
        AlertDialog(
            backgroundColor = AppTheme.RENDER_BACKGROUND_COLOR,
            modifier = Modifier
                .width(900.dp)
                .height(200.dp)
                .border(2.dp, AppTheme.RENDER_BORDER_COLOR, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            onDismissRequest = { },
            buttons = {},
            text = {
                Column{

                Box(modifier = Modifier.height(10.dp).fillMaxWidth())
                {
                    Text("")
                }
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Video export", color = AppTheme.RENDER_TEXT_COLOR)
                        Spacer(modifier = Modifier.width(30.dp))
                        LinearProgressIndicator(
                            progress = progress.value / 100f,
                            color = AppTheme.RENDER_ACTIVE_COLOR,
                            backgroundColor = AppTheme.RENDER_BAR_MAIN_COLOR,
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text("${progress.value.roundToInt()} %", color = AppTheme.RENDER_TEXT_COLOR)
                    }
                }
            }

        )
    }
}
