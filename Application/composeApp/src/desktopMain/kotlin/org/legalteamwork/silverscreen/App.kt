@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.legalteamwork.silverscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.legalteamwork.silverscreen.command.CommandWindowCompose
import org.legalteamwork.silverscreen.re.EditingPanel
import org.legalteamwork.silverscreen.resources.AppTheme
import org.legalteamwork.silverscreen.resources.Dimens
import org.legalteamwork.silverscreen.rm.ResourceManagerCompose
import org.legalteamwork.silverscreen.vp.VideoPanel
import org.legalteamwork.silverscreen.windows.*

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppScope.App() {
    val windowBlock = column(
        1f,
        row(
            0.7f,
            terminal(0.3f) {
                Box(
                    Modifier.fillMaxSize().background(
                        AppTheme.VIDEO_PANEL_BACKGROUND_COLOR, RoundedCornerShape(Dimens.WINDOW_CORNER_RADIUS)
                    )
                ) {
                    ResourceManagerCompose()
                }
            },
            terminal(0.6f) {
                Box(
                    Modifier.fillMaxSize().background(
                        AppTheme.VIDEO_PANEL_BACKGROUND_COLOR, RoundedCornerShape(Dimens.WINDOW_CORNER_RADIUS)
                    )
                ) {
                    VideoPanel.compose()
                }
            },
            terminal(0.1f) {
                Box(
                    Modifier.fillMaxSize().background(
                        AppTheme.VIDEO_PANEL_BACKGROUND_COLOR, RoundedCornerShape(Dimens.WINDOW_CORNER_RADIUS)
                    )
                ) {
                    CommandWindowCompose()
                }
            }
        ),
        terminal(0.3f) {
            Box(
                Modifier.fillMaxSize().background(
                    AppTheme.VIDEO_PANEL_BACKGROUND_COLOR, RoundedCornerShape(Dimens.WINDOW_CORNER_RADIUS)
                )
            ) {
                EditingPanel(height)
            }
        }
    )

    Surface(color = AppTheme.SURFACE_COLOR) {
        BoxWithConstraints(Modifier.fillMaxSize().padding(Dimens.MARGIN_SIZE)) {
            val dimensionsScope = DimensionsScope(maxWidth, maxHeight)
            windowBlock.content.invoke(dimensionsScope)
        }
    }
}

