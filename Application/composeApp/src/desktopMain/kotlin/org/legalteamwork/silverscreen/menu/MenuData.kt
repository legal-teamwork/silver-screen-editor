package org.legalteamwork.silverscreen.menu

import androidx.compose.ui.input.key.Key

data class MenuData(
    val text: String,
    val mnemonic: Key?,
    val enabled: Boolean,
    val menuItemList: List<MenuItemData>
) {
    constructor(
        text: String,
        mnemonic: Key? = null,
        enabled: Boolean = true,
        vararg menuItemList: MenuItemData
    ) : this(text, mnemonic, enabled, listOf(*menuItemList))
}
