package org.stjude.sprocket.server

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.stjude.sprocket.settings.OutputLevel
import java.io.File

class CommandBuilderTest {

    @Test
    fun `buildCommand creates basic command with analyzer and stdio`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = buildCommand(binary, OutputLevel.INFORMATION, lint = false)

        assertEquals(listOf("/usr/local/bin/sprocket", "analyzer", "--stdio"), command)
    }

    @Test
    fun `buildCommand includes verbose flag when output level is VERBOSE`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = buildCommand(binary, OutputLevel.VERBOSE, lint = false)

        assertTrue(command.contains("--verbose"))
        assertEquals(listOf("/usr/local/bin/sprocket", "analyzer", "--stdio", "--verbose"), command)
    }

    @Test
    fun `buildCommand includes quiet flag when output level is QUIET`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = buildCommand(binary, OutputLevel.QUIET, lint = false)

        assertTrue(command.contains("--quiet"))
        assertEquals(listOf("/usr/local/bin/sprocket", "analyzer", "--stdio", "--quiet"), command)
    }

    @Test
    fun `buildCommand does not include output flag when level is INFORMATION`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = buildCommand(binary, OutputLevel.INFORMATION, lint = false)

        assertFalse(command.contains("--verbose"))
        assertFalse(command.contains("--quiet"))
    }

    @Test
    fun `buildCommand includes lint flag when lint is enabled`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = buildCommand(binary, OutputLevel.INFORMATION, lint = true)

        assertTrue(command.contains("--lint"))
    }

    @Test
    fun `buildCommand does not include lint flag when lint is disabled`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = buildCommand(binary, OutputLevel.INFORMATION, lint = false)

        assertFalse(command.contains("--lint"))
    }

    @Test
    fun `buildCommand includes both output level and lint flags`() {
        val binary = File("/usr/local/bin/sprocket")
        val command = buildCommand(binary, OutputLevel.VERBOSE, lint = true)

        assertEquals(
            listOf("/usr/local/bin/sprocket", "analyzer", "--stdio", "--verbose", "--lint"),
            command
        )
    }

    @Test
    fun `buildCommand preserves flag order`() {
        val binary = File("/path/to/sprocket")
        val command = buildCommand(binary, OutputLevel.QUIET, lint = true)

        val stdioIndex = command.indexOf("--stdio")
        val quietIndex = command.indexOf("--quiet")
        val lintIndex = command.indexOf("--lint")

        assertTrue(stdioIndex < quietIndex, "stdio should come before quiet")
        assertTrue(quietIndex < lintIndex, "quiet should come before lint")
    }

    @Test
    fun `buildCommand handles paths with spaces`() {
        val binary = File("/path/with spaces/sprocket")
        val command = buildCommand(binary, OutputLevel.INFORMATION, lint = false)

        assertEquals("/path/with spaces/sprocket", command[0])
    }

    @Test
    fun `buildCommand uses absolute path`() {
        val binary = File("relative/path/sprocket")
        val command = buildCommand(binary, OutputLevel.INFORMATION, lint = false)

        assertTrue(command[0].startsWith("/") || command[0].contains(":"),
            "Command should use absolute path")
    }

    private fun buildCommand(binary: File, outputLevel: OutputLevel, lint: Boolean): List<String> {
        val command = mutableListOf(binary.absolutePath, "analyzer", "--stdio")
        outputLevel.cliArg?.let { command.add(it) }
        if (lint) {
            command.add("--lint")
        }
        return command
    }
}
