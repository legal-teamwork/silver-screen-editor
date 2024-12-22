package org.legalteamwork.silverscreen.rm.window.source

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.rm.ResourceManager

@Composable
fun AppScope.SourceAddButton() {
    Box(
        modifier = Modifier.padding(Dimens.CELL_PADDING).fillMaxSize()
    ) {
        Column{
            Box(modifier =
            Modifier
                .background(color = Color(0xFF21282D), shape = RoundedCornerShape(8.dp))
                .size(Dimens.IMAGE_WIDTH, Dimens.IMAGE_HEIGHT)
                .clickable(
                    onClickLabel = "Add resource",
                    role = Role.Image,
                    onClick = { ResourceManager.addSourceTriggerActivity(commandManager) },
                ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource("svg/plus_big_button.svg"),
                    contentDescription = "Add resource button",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color(0xFFFFFFFF))
                )
            }
            Box(
                modifier = Modifier
                    .width(Dimens.IMAGE_WIDTH)
                    .height(3.dp)
            )
            Text(
                text = "Import files",
                color = Color(0xFFFFFFFF)
            )
        }

    }
}
