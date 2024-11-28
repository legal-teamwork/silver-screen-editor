package org.legalteamwork.silverscreen.command

import io.github.oshai.kotlinlogging.KLogger

fun KLogger.commandLog(commandName: String) = this.info { "`$commandName` command executed" }
