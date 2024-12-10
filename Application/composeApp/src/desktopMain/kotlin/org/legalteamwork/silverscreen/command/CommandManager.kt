package org.legalteamwork.silverscreen.command

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CommandManager(
    private val stackMaxSize: Int = 100
) {
    val stack = mutableStateListOf<CommandUndoSupport>()
    val pointer = mutableStateOf(0)

    fun execute(command: Command) = command.execute()

    fun execute(command: CommandUndoSupport) {
        addCommandToStack(command)

        command.execute()
    }

    fun undo() {
        var pointerEditor by pointer

        if (pointerEditor >= 1)
            stack[--pointerEditor].undo()
    }

    fun redo() {
        var pointerEditor by pointer

        if (stack.size > 0 && pointerEditor < stack.size)
            stack[pointerEditor++].execute()
    }

    fun seek(newPointer: Int) {
        assert(newPointer in 0..stack.size)

        while (pointer.value < newPointer) {
            redo()
        }

        while (pointer.value > newPointer) {
            undo()
        }
    }

    private fun addCommandToStack(command: CommandUndoSupport) {
        var pointerEditor by pointer

        while (stack.size > pointerEditor)
            stack.removeLast()

        stack.add(command)
        pointerEditor++

        while (stack.size > stackMaxSize) {
            stack.removeFirst()
            pointerEditor--
        }
    }
}