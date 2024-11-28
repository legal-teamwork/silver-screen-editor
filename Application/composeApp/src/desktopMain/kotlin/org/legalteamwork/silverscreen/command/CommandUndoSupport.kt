package org.legalteamwork.silverscreen.command

interface CommandUndoSupport : Command {
    fun undo()
}