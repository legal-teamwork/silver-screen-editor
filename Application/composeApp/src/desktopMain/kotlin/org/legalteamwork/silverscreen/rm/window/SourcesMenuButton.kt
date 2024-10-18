package org.legalteamwork.silverscreen.rm.window

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.window.resource.SimpleResource

val resources = listOf(
    SimpleResource("Untitled1.mp4", "tmp-resources/u1.png"),
    SimpleResource("Untitled2.mp4", "tmp-resources/u2.png"),
    SimpleResource("Untitled3.mp4", "tmp-resources/u3.png")
)

@Composable
fun SourcesMenuButton() {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            for (resourcesRow in resources.chunked(2)) {
                item {
                    Row(
                        modifier = Modifier.height(300.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (resource in resourcesRow) {
                            Column(
                                modifier = Modifier.fillMaxHeight().fillParentMaxWidth(0.45f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                Image(
                                    painter = painterResource(resource.resourcePath),
                                    contentDescription = resource.title,
                                    modifier = Modifier.height(270.dp).fillMaxWidth(),
                                    contentScale = ContentScale.Fit
                                )
                                BasicText(resource.title, color = { Color.White })
                            }
                        }
                    }
                }
            }
        }
    }
}
