package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.resource.Resource

@Composable
fun ResourcePropertiesContextWindow(resource: Resource) {
    ResourceContextWindowPattern {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Properties for the ${resource.title} resource", modifier = Modifier.padding(5.dp))
        }
    }
}