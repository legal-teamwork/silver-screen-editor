package org.legalteamwork.silverscreen.re

import org.legalteamwork.silverscreen.re.VideoTrack.highlightedResources


fun VideoEditor.highlightResource(id: Int) {
    highlightedResources[id] = !highlightedResources[id]
}

fun VideoEditor.getHighlightedResources() = highlightedResources

fun VideoEditor.resetHighlighting() {
    highlightedResources.fill(false)
}



