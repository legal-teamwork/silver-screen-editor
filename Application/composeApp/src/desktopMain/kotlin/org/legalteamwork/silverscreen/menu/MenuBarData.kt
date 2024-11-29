package org.legalteamwork.silverscreen.menu

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class MenuBarData(
    var activeMenu: MutableState<MenuData?>, val menuList: List<MenuData>
) {
    constructor(activeMenu: MenuData?, vararg menuList: MenuData) : this(
        mutableStateOf(activeMenu),
        listOf(*menuList)
    )
}
