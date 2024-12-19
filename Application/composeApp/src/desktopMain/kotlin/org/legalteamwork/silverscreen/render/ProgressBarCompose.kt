package org.legalteamwork.silverscreen.render

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import kotlin.math.roundToInt

@Composable
fun ExportProgressDialog(isExporting: MutableState<Boolean>, progress: MutableState<Float>) {
    if (isExporting.value) {
        AlertDialog(
            onDismissRequest = { },
            buttons = {},
            title = { Text("Video export") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(progress = progress.value / 100f)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("${progress.value.roundToInt()} %")
                }
            }
        )
    }
}