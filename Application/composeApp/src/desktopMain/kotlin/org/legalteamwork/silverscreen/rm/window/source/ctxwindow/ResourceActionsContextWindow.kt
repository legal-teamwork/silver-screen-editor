package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.oshai.kotlinlogging.KotlinLogging
import org.legalteamwork.silverscreen.AppScope
import org.legalteamwork.silverscreen.command.global.RemoveResourceCommand
import org.legalteamwork.silverscreen.resources.ResourceActionsContextWindowTheme
import org.legalteamwork.silverscreen.rm.ResourceManager

private val logger = KotlinLogging.logger {  }

@Composable
fun AppScope.ResourceActionsContextWindow(
    contextWindowData: ContextWindowData,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit,
) {
    val resource = ResourceManager.activeResource.value ?: return
    val position = contextWindowData.position
    
    ResourceContextWindowPattern(position, onContextWindowOpen, onContextWindowClose) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ResourceAction("Delete") {
                logger.info { "Delete context button clicked" }
                commandManager.execute(RemoveResourceCommand(resourceManager, resource))
                onContextWindowClose()
            }

            Divider(modifier = Modifier.fillMaxWidth(), color = ResourceActionsContextWindowTheme.DIVIDERS_COLOR)

            ResourceAction("Move to") {
                logger.info { "Move to context button clicked" }
                onContextWindowOpen(ContextWindow(ContextWindowId.MOVE_TO, contextWindowData))
            }

            Divider(modifier = Modifier.fillMaxWidth(), color = ResourceActionsContextWindowTheme.DIVIDERS_COLOR)

            ResourceAction("Properties") {
                logger.info { "Properties context button clicked" }
                onContextWindowOpen(ContextWindow(ContextWindowId.PROPERTIES, contextWindowData))
            }
        }
    }
}

@Composable
private fun ResourceAction(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(text = text, modifier = Modifier.padding(5.dp), color = ResourceActionsContextWindowTheme.TEXT_COLOR)
    }
}
