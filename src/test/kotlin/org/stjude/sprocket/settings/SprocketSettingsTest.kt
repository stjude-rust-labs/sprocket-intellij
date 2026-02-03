package org.stjude.sprocket.settings

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class SprocketSettingsTest {

    private lateinit var settings: SprocketSettings

    @BeforeEach
    fun setUp() {
        settings = SprocketSettings()
    }

    @Test
    fun `default binaryPath is empty`() {
        assertEquals("", settings.binaryPath)
    }

    @Test
    fun `default outputLevel is QUIET`() {
        assertEquals(OutputLevel.QUIET, settings.outputLevel)
    }

    @Test
    fun `default lint is false`() {
        assertFalse(settings.lint)
    }

    @Test
    fun `getState returns this instance`() {
        assertSame(settings, settings.state)
    }

    @Test
    fun `loadState copies all properties`() {
        val source = SprocketSettings().apply {
            binaryPath = "/custom/path/sprocket"
            outputLevel = OutputLevel.VERBOSE
            lint = true
        }

        settings.loadState(source)

        assertEquals("/custom/path/sprocket", settings.binaryPath)
        assertEquals(OutputLevel.VERBOSE, settings.outputLevel)
        assertTrue(settings.lint)
    }

    @Test
    fun `settings properties are mutable`() {
        settings.binaryPath = "/new/path"
        settings.outputLevel = OutputLevel.INFORMATION
        settings.lint = true

        assertEquals("/new/path", settings.binaryPath)
        assertEquals(OutputLevel.INFORMATION, settings.outputLevel)
        assertTrue(settings.lint)
    }

    @Test
    fun `binaryPath can be set to blank`() {
        settings.binaryPath = "/some/path"
        settings.binaryPath = ""
        assertEquals("", settings.binaryPath)
    }

    @Test
    fun `binaryPath handles paths with spaces`() {
        val pathWithSpaces = "/path/with spaces/to/sprocket"
        settings.binaryPath = pathWithSpaces
        assertEquals(pathWithSpaces, settings.binaryPath)
    }

    @Test
    fun `outputLevel can cycle through all values`() {
        for (level in OutputLevel.entries) {
            settings.outputLevel = level
            assertEquals(level, settings.outputLevel)
        }
    }
}
