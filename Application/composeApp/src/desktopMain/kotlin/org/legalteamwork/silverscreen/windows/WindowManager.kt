package org.legalteamwork.silverscreen.windows

import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.Dp

class WindowManager(
    val windowBlock: WindowBlock
) {
    private val deltaWidthList: MutableList<MutableState<Dp>> = mutableListOf()
    private val deltaHeightList: MutableList<MutableState<Dp>> = mutableListOf()

    fun configure() {
        windowBlock.configure(deltaWidthList::add, deltaHeightList::add)
    }

}