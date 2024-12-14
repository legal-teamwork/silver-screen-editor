package org.legalteamwork.silverscreen.rm.window.source

import org.legalteamwork.silverscreen.rm.resource.Resource
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.util.*

class ResourceTransferable(private val resource: Resource) : Transferable {
    private val flavors = arrayOf(
        DataFlavor(
            DataFlavor.javaSerializedObjectMimeType + ";class=org.legalteamwork.silverscreen.rm.resource.Resource",
            "Resource"
        )
    )

    override fun getTransferDataFlavors(): Array<DataFlavor> = flavors.clone()

    override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean = flavors.toList().any { it.equals(flavor) }

    override fun getTransferData(flavor: DataFlavor?): Any {
        if (Objects.equals(flavor, flavors[0])) {
            return resource
        } else {
            throw UnsupportedFlavorException(flavor)
        }
    }
}
