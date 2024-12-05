package org.legalteamwork.silverscreen.windows.data

import org.legalteamwork.silverscreen.windows.block.WindowBlock

data class BlockWithWeight(
    /**
     * Вес окошка, подсчет размера происходит, используя данный параметр
     */
    val weight: Float,
    /**
     * Само окошко
     */
    val block: WindowBlock,
)
