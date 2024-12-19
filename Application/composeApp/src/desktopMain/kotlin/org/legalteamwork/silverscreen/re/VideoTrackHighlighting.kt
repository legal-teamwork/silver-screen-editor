package org.legalteamwork.silverscreen.re

import org.legalteamwork.silverscreen.re.VideoTrack.highlightedResources


fun VideoEditor.highlightResource(id: Int) : Boolean {
    println(highlightedResources)
    if (highlightedResources.contains(id)) {
        highlightedResources.remove(id)
        return false
    }
    else {
        highlightedResources.add(id)
        return true
    }
}

fun VideoEditor.getHighlightedResources() = highlightedResources

fun VideoEditor.resetHighlighting() {
    highlightedResources.clear()
}



