package org.legalteamwork.silverscreen.rm.window.effects

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.util.Objects

class EffectTransferable(val videoEffect: VideoEffect) : Transferable {
    private val flavors = arrayOf(
        DataFlavor(
            DataFlavor.javaSerializedObjectMimeType + ";class=org.legalteamwork.silverscreen.rm.window.effects.VideoEffect",
            "VideoEffect"
        )
    )

    override fun getTransferDataFlavors(): Array<DataFlavor> = flavors.clone()

    override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean = flavors.toList().any { it.equals(flavor) }

    override fun getTransferData(flavor: DataFlavor?): Any {
        if (Objects.equals(flavor, flavors[0])) {
            return videoEffect
        } else {
            throw UnsupportedFlavorException(flavor)
        }
    }
}