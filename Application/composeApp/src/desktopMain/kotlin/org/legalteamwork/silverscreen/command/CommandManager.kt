package org.legalteamwork.silverscreen.command

class CommandManager {

    private val commandUndoSupportStack = mutableListOf<CommandUndoSupport>()

    fun execute(command: Command) = command.execute()

    fun execute(command: CommandUndoSupport) {
        addCommandToStack(command)

        command.execute()
    }

    fun undo() = commandUndoSupportStack.removeLastOrNull()?.undo()

    private fun addCommandToStack(command: CommandUndoSupport) {
        commandUndoSupportStack.add(command)

        while (commandUndoSupportStack.size > STACK_MAX_SIZE) {
            commandUndoSupportStack.removeFirst()
        }
    }

    companion object {
        private const val STACK_MAX_SIZE = 5
    }
}