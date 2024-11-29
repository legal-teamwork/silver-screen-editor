package org.legalteamwork.silverscreen.command

class CommandManager(
    private val stackMaxSize: Int = 100
) {
    private val stack = mutableListOf<CommandUndoSupport>()
    private var pointer = 0

    fun execute(command: Command) = command.execute()

    fun execute(command: CommandUndoSupport) {
        addCommandToStack(command)

        command.execute()
    }

    fun undo() {
        if (pointer > 1)
            stack[--pointer].undo()
    }

    fun redo() {
        if (stack.size > 0 && pointer < stack.size)
            stack[pointer++].execute()
    }

    private fun addCommandToStack(command: CommandUndoSupport) {
        while (stack.size > pointer)
            stack.removeLast()

        stack.add(command)
        pointer++

        while (stack.size > stackMaxSize) {
            stack.removeFirst()
            pointer--
        }
    }
}