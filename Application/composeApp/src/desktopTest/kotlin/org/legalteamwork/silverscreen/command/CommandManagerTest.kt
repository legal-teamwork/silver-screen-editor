package org.legalteamwork.silverscreen.command

import kotlin.test.Test
import kotlin.test.assertEquals

class CommandManagerTest {

    @Test
    fun redoUndoTest() {
        val commandManager = CommandManager(Int.MAX_VALUE)
        val testContext = TestContext(0)
        commandManager.execute(IncrementCommand(testContext))
        commandManager.execute(IncrementCommand(testContext))
        commandManager.execute(IncrementCommand(testContext))
        assertEquals(3, testContext.value)
        repeat(2) { commandManager.undo() }
        assertEquals(1, testContext.value)
        commandManager.redo()
        assertEquals(2, testContext.value)
    }
}

data class TestContext(var value: Int)

private class IncrementCommand(
    private val testContext: TestContext
) : CommandUndoSupport {
    override val title: String = ""
    override val description: String = ""

    override fun execute() {
        testContext.value++
    }

    override fun undo() {
        testContext.value--
    }
}