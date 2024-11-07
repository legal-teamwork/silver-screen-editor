package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.legalteamwork.silverscreen.rm.ResourceManager

@Composable
fun ResourceActionsContextWindow(
    contextWindowData: ContextWindowData,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit = { onContextWindowOpen(null) }
) {
    val resource = contextWindowData.resource
    val position = contextWindowData.position
    
    ResourceContextWindowPattern(position, onContextWindowOpen, onContextWindowClose) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ResourceAction("Clone") {
                ResourceManager.addSource(resource.clone())
                onContextWindowClose()
            }
            ResourceAction("Delete") {
                ResourceManager.removeSource(resource)
                onContextWindowClose()
            }
            ResourceAction("Properties") {
                onContextWindowOpen(ContextWindow(ContextWindow.PROPERTIES, contextWindowData))
            }
        }
    }
}

@Composable
private fun ResourceAction(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(text = text, modifier = Modifier.padding(5.dp))
    }
}
