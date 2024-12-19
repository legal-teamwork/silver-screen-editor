package org.legalteamwork.silverscreen.re

import org.legalteamwork.silverscreen.re.VideoTrack.highlightedResources


fun VideoEditor.highlightResource(id: Int) : Boolean {
    if (highlightedResources.contains(id)) {
        highlightedResources.remove(id)
        println(highlightedResources)
        return false
    }
    else {
        highlightedResources.add(id)
        println(highlightedResources)
        return true
    }
}

fun VideoEditor.getHighlightedResources() = highlightedResources



