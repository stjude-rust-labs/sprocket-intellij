package org.stjude.sprocket.server

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ServerStateTest {

    @Test
    fun `all expected states exist`() {
        val states = ServerState.entries
        assertEquals(4, states.size)
        assertTrue(states.contains(ServerState.STOPPED))
        assertTrue(states.contains(ServerState.STARTING))
        assertTrue(states.contains(ServerState.RUNNING))
        assertTrue(states.contains(ServerState.ERROR))
    }

    @Test
    fun `states have distinct ordinal values`() {
        val ordinals = ServerState.entries.map { it.ordinal }.toSet()
        assertEquals(ServerState.entries.size, ordinals.size)
    }

    @Test
    fun `valueOf works for all states`() {
        assertEquals(ServerState.STOPPED, ServerState.valueOf("STOPPED"))
        assertEquals(ServerState.STARTING, ServerState.valueOf("STARTING"))
        assertEquals(ServerState.RUNNING, ServerState.valueOf("RUNNING"))
        assertEquals(ServerState.ERROR, ServerState.valueOf("ERROR"))
    }

    @Test
    fun `name returns expected string`() {
        assertEquals("STOPPED", ServerState.STOPPED.name)
        assertEquals("STARTING", ServerState.STARTING.name)
        assertEquals("RUNNING", ServerState.RUNNING.name)
        assertEquals("ERROR", ServerState.ERROR.name)
    }
}
