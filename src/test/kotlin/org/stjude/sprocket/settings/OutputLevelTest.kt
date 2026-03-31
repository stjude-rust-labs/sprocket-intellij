package org.stjude.sprocket.settings

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class OutputLevelTest {

    @Test
    fun `VERBOSE has correct CLI argument`() {
        assertEquals("--verbose", OutputLevel.INFO.cliArg)
    }

    @Test
    fun `INFORMATION has null CLI argument`() {
        assertNull(OutputLevel.WARN.cliArg)
    }

    @Test
    fun `QUIET has correct CLI argument`() {
        assertEquals("--quiet", OutputLevel.ERROR.cliArg)
    }

    @Test
    fun `toString returns display name`() {
        assertEquals("Info", OutputLevel.INFO.toString())
        assertEquals("Warn", OutputLevel.WARN.toString())
        assertEquals("Error", OutputLevel.ERROR.toString())
    }

    @Test
    fun `all output levels have display names`() {
        OutputLevel.entries.forEach { level ->
            assertNotNull(level.displayName)
            assertTrue(level.displayName.isNotBlank())
        }
    }

    @Test
    fun `entries contains all expected values`() {
        val entries = OutputLevel.entries
        assertEquals(5, entries.size)
        assertTrue(entries.contains(OutputLevel.TRACE))
        assertTrue(entries.contains(OutputLevel.DEBUG))
        assertTrue(entries.contains(OutputLevel.INFO))
        assertTrue(entries.contains(OutputLevel.WARN))
        assertTrue(entries.contains(OutputLevel.ERROR))
    }
}
