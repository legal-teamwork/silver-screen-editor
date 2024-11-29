package org.legalteamwork.silverscreen.command

class CommandManager(
    private val stackMaxSize: Int = 5
) {
    private val commandUndoSupportStack = mutableListOf<CommandUndoSupport>()
    private var commandUndoSupportPointer = 0

    fun execute(command: Command) = command.execute()

    fun execute(command: CommandUndoSupport) {
        addCommandToStack(command)

        command.execute()
    }

    fun undo() {
        if (commandUndoSupportPointer > 1)
            commandUndoSupportStack[--commandUndoSupportPointer].undo()
    }

    fun redo() {
        if (commandUndoSupportStack.size > 0 && commandUndoSupportPointer < commandUndoSupportStack.size)
            commandUndoSupportStack[commandUndoSupportPointer++].execute()
    }

    private fun addCommandToStack(command: CommandUndoSupport) {
        while (commandUndoSupportStack.size > commandUndoSupportPointer)
            commandUndoSupportStack.removeLast()

        commandUndoSupportStack.add(command)
        commandUndoSupportPointer++

        while (commandUndoSupportStack.size > stackMaxSize) {
            commandUndoSupportStack.removeFirst()
            commandUndoSupportPointer--
        }
    }
}