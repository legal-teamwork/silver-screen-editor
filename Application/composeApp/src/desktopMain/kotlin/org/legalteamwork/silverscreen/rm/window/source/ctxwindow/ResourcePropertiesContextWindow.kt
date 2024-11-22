package org.legalteamwork.silverscreen.rm.window.source.ctxwindow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.legalteamwork.silverscreen.resources.ResourcePropertiesContextWindowTheme
import org.legalteamwork.silverscreen.rm.ResourceManager

@Composable
fun ResourcePropertiesContextWindow(
    contextWindowData: ContextWindowData,
    onContextWindowOpen: (ContextWindow?) -> Unit,
    onContextWindowClose: () -> Unit,
) {
    val resource = ResourceManager.activeResource.value ?: return
    val position = contextWindowData.position
    val properties = resource.properties

    ResourceContextWindowPattern(position, onContextWindowOpen, onContextWindowClose) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                properties.properties
                    .groupBy { it.groupName }
                    .forEach { (groupName, groupProperties) ->
                        Box(
                            Modifier.fillMaxWidth().wrapContentHeight()
                                .border(1.dp, SolidColor(ResourcePropertiesContextWindowTheme.BORDER_COLOR), RectangleShape)
                        ) {
                            Text(
                                groupName,
                                modifier = Modifier.padding(10.dp, 2.dp),
                                fontSize = 8.sp,
                                color = ResourcePropertiesContextWindowTheme.PROPERTY_NAME_COLOR
                            )
                        }

                        groupProperties.forEach { property ->
                            Box(Modifier.fillMaxWidth().wrapContentHeight()) {
                                Text(
                                    "${property.key} - ${property.value}",
                                    modifier = Modifier.padding(7.dp, 2.dp),
                                    fontSize = 10.sp,
                                    color = ResourcePropertiesContextWindowTheme.EACH_PROPERTY_TEXT_COLOR
                                )
                            }
                        }
                    }
            }
        }
    }
}