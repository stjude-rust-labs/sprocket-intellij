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
        assertEquals("", settings.binaryPath())
    }

    @Test
    fun `default outputLevel is QUIET`() {
        assertEquals(OutputLevel.QUIET, settings.outputLevel())
    }

    @Test
    fun `default lint is false`() {
        assertFalse(settings.lint())
    }

    @Test
    fun `loadState copies all properties`() {
        val source = SprocketSettings().state.apply {
            binaryPath = "/custom/path/sprocket"
            options.outputLevel = OutputLevel.VERBOSE
            options.lintOptions.enabled = true
        }

        settings.loadState(source)

        assertEquals("/custom/path/sprocket", settings.binaryPath())
        assertEquals(OutputLevel.VERBOSE, settings.outputLevel())
        assertTrue(settings.lint())
    }

    @Test
    fun `settings properties are mutable`() {
        settings.state.binaryPath = "/new/path"
        settings.state.options.outputLevel = OutputLevel.INFORMATION
        settings.state.options.lintOptions.enabled = true

        assertEquals("/new/path", settings.binaryPath())
        assertEquals(OutputLevel.INFORMATION, settings.outputLevel())
        assertTrue(settings.lint())
    }

    @Test
    fun `binaryPath can be set to blank`() {
        settings.state.binaryPath = "/some/path"
        settings.state.binaryPath = ""
        assertEquals("", settings.binaryPath())
    }

    @Test
    fun `binaryPath handles paths with spaces`() {
        val pathWithSpaces = "/path/with spaces/to/sprocket"
        settings.state.binaryPath = pathWithSpaces
        assertEquals(pathWithSpaces, settings.binaryPath())
    }

    @Test
    fun `outputLevel can cycle through all values`() {
        for (level in OutputLevel.entries) {
            settings.state.options.outputLevel = level
            assertEquals(level, settings.state.options.outputLevel)
        }
    }
}
