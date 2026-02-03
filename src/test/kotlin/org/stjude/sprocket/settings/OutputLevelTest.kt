package org.stjude.sprocket.settings

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class OutputLevelTest {

    @Test
    fun `VERBOSE has correct CLI argument`() {
        assertEquals("--verbose", OutputLevel.VERBOSE.cliArg)
    }

    @Test
    fun `INFORMATION has null CLI argument`() {
        assertNull(OutputLevel.INFORMATION.cliArg)
    }

    @Test
    fun `QUIET has correct CLI argument`() {
        assertEquals("--quiet", OutputLevel.QUIET.cliArg)
    }

    @Test
    fun `toString returns display name`() {
        assertEquals("Verbose", OutputLevel.VERBOSE.toString())
        assertEquals("Information", OutputLevel.INFORMATION.toString())
        assertEquals("Quiet", OutputLevel.QUIET.toString())
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
        assertEquals(3, entries.size)
        assertTrue(entries.contains(OutputLevel.VERBOSE))
        assertTrue(entries.contains(OutputLevel.INFORMATION))
        assertTrue(entries.contains(OutputLevel.QUIET))
    }
}
