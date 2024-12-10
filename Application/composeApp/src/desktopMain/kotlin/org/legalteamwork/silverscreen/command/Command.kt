package org.legalteamwork.silverscreen.command

interface Command {
    val title: String
    val description: String

    fun execute()
}