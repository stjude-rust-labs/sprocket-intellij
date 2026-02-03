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
    fun `default checkForUpdates is true`() {
        assertTrue(settings.checkForUpdates)
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
    fun `default maxRetries is 1`() {
        assertEquals(1, settings.maxRetries)
    }

    @Test
    fun `getState returns this instance`() {
        assertSame(settings, settings.state)
    }

    @Test
    fun `loadState copies all properties`() {
        val source = SprocketSettings().apply {
            checkForUpdates = false
            binaryPath = "/custom/path/sprocket"
            outputLevel = OutputLevel.VERBOSE
            lint = true
            maxRetries = 5
        }

        settings.loadState(source)

        assertFalse(settings.checkForUpdates)
        assertEquals("/custom/path/sprocket", settings.binaryPath)
        assertEquals(OutputLevel.VERBOSE, settings.outputLevel)
        assertTrue(settings.lint)
        assertEquals(5, settings.maxRetries)
    }

    @Test
    fun `settings properties are mutable`() {
        settings.checkForUpdates = false
        settings.binaryPath = "/new/path"
        settings.outputLevel = OutputLevel.INFORMATION
        settings.lint = true
        settings.maxRetries = 3

        assertFalse(settings.checkForUpdates)
        assertEquals("/new/path", settings.binaryPath)
        assertEquals(OutputLevel.INFORMATION, settings.outputLevel)
        assertTrue(settings.lint)
        assertEquals(3, settings.maxRetries)
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

    @Test
    fun `maxRetries accepts zero`() {
        settings.maxRetries = 0
        assertEquals(0, settings.maxRetries)
    }

    @Test
    fun `maxRetries accepts negative values`() {
        settings.maxRetries = -1
        assertEquals(-1, settings.maxRetries)
    }
}
