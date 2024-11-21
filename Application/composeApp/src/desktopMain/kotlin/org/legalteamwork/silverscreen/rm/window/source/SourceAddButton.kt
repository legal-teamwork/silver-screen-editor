package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.rm.ResourceManager

@Composable
fun SourceAddButton() {
    Box(
        modifier = Modifier.padding(Dimens.CELL_PADDING).fillMaxSize()
    ) {
        Box(
            Modifier
                .size(Dimens.IMAGE_WIDTH, Dimens.IMAGE_HEIGHT)
                .align(Alignment.Center)
                .clickable(
                    onClickLabel = "Add resource",
                    role = Role.Image,
                    onClick = ResourceManager::addSourceTriggerActivity,
                ),
        ) {
            Image(
                painter = painterResource("svg/add-resource.svg"),
                contentDescription = "Add resource button",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
            )
        }
    }
}
