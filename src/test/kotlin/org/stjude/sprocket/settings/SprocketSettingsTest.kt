package org.stjude.sprocket.settings

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    fun `default outputLevel is ERROR`() {
        assertEquals(OutputLevel.ERROR, settings.outputLevel())
    }

    @Test
    fun `default lint is false`() {
        assertFalse(settings.lint())
    }

    @Test
    fun `loadState copies all properties`() {
        val source =
            SprocketSettings().state.apply {
                binaryPath = "/custom/path/sprocket"
                options.outputLevel = OutputLevel.INFO
                options.lintOptions.enabled = true
            }

        settings.loadState(source)

        assertEquals("/custom/path/sprocket", settings.binaryPath())
        assertEquals(OutputLevel.INFO, settings.outputLevel())
        assertTrue(settings.lint())
    }

    @Test
    fun `settings properties are mutable`() {
        settings.state.binaryPath = "/new/path"
        settings.state.options.outputLevel = OutputLevel.WARN
        settings.state.options.lintOptions.enabled = true

        assertEquals("/new/path", settings.binaryPath())
        assertEquals(OutputLevel.WARN, settings.outputLevel())
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
            assertEquals(level, settings.outputLevel())
        }
    }
}
