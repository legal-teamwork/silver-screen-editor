package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager

@Composable
fun ContextWindowScope.ResourceActionsContextWindow() {
    val resource = ResourceManager.activeResource.value ?: return
    
    ResourceContextWindowPattern {
        Column(modifier = Modifier.fillMaxWidth()) {
            ResourceAction("Clone") {
                ResourceManager.addSource(resource.clone())
                onContextWindowClose()
            }
            ResourceAction("Delete") {
                ResourceManager.removeSource(resource)
                onContextWindowClose()
            }

            Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)

            ResourceAction("Move to") {
                onContextWindowOpen { MoveToWindow() }
            }
            ResourceAction("Copy to") {
                onContextWindowOpen { CopyToWindow() }
            }

            Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)

            ResourceAction("Properties") {
                onContextWindowOpen { ResourcePropertiesContextWindow() }
            }
        }
    }
}

@Composable
private fun ResourceAction(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(text = text, modifier = Modifier.padding(5.dp), color = Color.White)
    }
}
