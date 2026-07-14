package org.stjude.sprocket.server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.stjude.sprocket.cli.SprocketCommand
import org.stjude.sprocket.settings.OutputLevel
import java.io.File

class CommandBuilderTest {
    @Test
    fun `serverCommand creates basic command with analyzer and stdio`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = serverCommand(binary, OutputLevel.WARN, lint = false)

        assertEquals(listOf("/usr/local/bin/sprocket", "analyzer", "--stdio"), command)
    }

    @Test
    fun `serverCommand includes verbose flag when output level is INFO`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = serverCommand(binary, OutputLevel.INFO, lint = false)

        assertTrue(command.contains("--verbose"))
        assertEquals(listOf("/usr/local/bin/sprocket", "--verbose", "analyzer", "--stdio"), command)
    }

    @Test
    fun `serverCommand includes quiet flag when output level is ERROR`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = serverCommand(binary, OutputLevel.ERROR, lint = false)

        assertTrue(command.contains("--quiet"))
        assertEquals(listOf("/usr/local/bin/sprocket", "--quiet", "analyzer", "--stdio"), command)
    }

    @Test
    fun `serverCommand does not include output flag when level is WARN`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = serverCommand(binary, OutputLevel.WARN, lint = false)

        assertFalse(command.contains("--verbose"))
        assertFalse(command.contains("--quiet"))
    }

    @Test
    fun `serverCommand includes lint flag when lint is enabled`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = serverCommand(binary, OutputLevel.WARN, lint = true)

        assertTrue(command.contains("--lint"))
    }

    @Test
    fun `serverCommand does not include lint flag when lint is disabled`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = serverCommand(binary, OutputLevel.WARN, lint = false)

        assertFalse(command.contains("--lint"))
    }

    @Test
    fun `serverCommand includes both output level and lint flags`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = serverCommand(binary, OutputLevel.INFO, lint = true)

        assertEquals(
            listOf("/usr/local/bin/sprocket", "--verbose", "analyzer", "--stdio", "--lint"),
            command,
        )
    }

    @Test
    fun `serverCommand puts the global verbosity flag before the subcommand`() {
        val binary = File("/path/to/sprocket")
        val command = serverCommand(binary, OutputLevel.ERROR, lint = true)

        val quietIndex = command.indexOf("--quiet")
        val stdioIndex = command.indexOf("--stdio")
        val lintIndex = command.indexOf("--lint")

        assertTrue(quietIndex < stdioIndex, "verbosity flag should come before stdio")
        assertTrue(stdioIndex < lintIndex, "stdio should come before lint")
    }

    @Test
    fun `serverCommand handles paths with spaces`() {
        val binary = File("/path/with spaces/sprocket")
        val command = serverCommand(binary, OutputLevel.WARN, lint = false)

        assertEquals("/path/with spaces/sprocket", command[0])
    }

    @Test
    fun `serverCommand uses absolute path`() {
        val binary = File("relative/path/sprocket")
        val command = serverCommand(binary, OutputLevel.WARN, lint = false)

        assertTrue(
            command[0].startsWith("/") || command[0].contains(":"),
            "Command should use absolute path",
        )
    }

    private fun serverCommand(
        binary: File,
        outputLevel: OutputLevel,
        lint: Boolean,
    ): List<String> = SprocketCommand.serverCommand(binary, outputLevel, lint, workDirectory = null).getCommandLineList(null)
}
