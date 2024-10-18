package org.legalteamwork.silverscreen.rm.window

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.resource.SimpleResource

const val COLUMNS_NUMBER = 2
val resources = listOf(
    SimpleResource("Untitled1.mp4", "tmp-resources/u1.png"),
    SimpleResource("Untitled2.mp4", "tmp-resources/u2.png"),
    SimpleResource("Untitled3.mp4", "tmp-resources/u3.png")
)

@Composable
fun SourcesMenuButton() {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Fixed(COLUMNS_NUMBER)) {
            for (resource in resources) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Image(
                            painter = painterResource(resource.resourcePath),
                            contentDescription = resource.title,
                            modifier = Modifier.height(275.dp).fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )
                        BasicText(resource.title, color = { Color.White })
                    }
                }
            }
        }
    }
}
