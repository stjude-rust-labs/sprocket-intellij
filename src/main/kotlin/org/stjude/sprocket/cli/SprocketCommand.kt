package org.stjude.sprocket.cli

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import org.stjude.sprocket.server.SprocketServerManager
import org.stjude.sprocket.settings.OutputLevel
import org.stjude.sprocket.settings.SprocketSettings
import java.io.File

/**
 * Utilities for building `GeneralCommandLine`s for the `sprocket` binary.
 */
class SprocketCommand(val project: Project) {
    val projectSettings = SprocketSettings.getInstance(project)
    val sprocketBinary = SprocketServerManager.getInstance().resolveBinary(project)

    fun server(): GeneralCommandLine? {
        val binary = sprocketBinary ?: return null
        return serverCommand(binary, projectSettings.outputLevel(), projectSettings.lint(), project.basePath)
    }

    companion object {
        /**
         * Builds the `sprocket analyzer --stdio` server command. The verbosity flag is a
         * global option, so it precedes the `analyzer` subcommand.
         */
        fun serverCommand(
            binary: File,
            outputLevel: OutputLevel,
            lint: Boolean,
            workDirectory: String?,
        ): GeneralCommandLine {
            val command = GeneralCommandLine(binary.absolutePath)
                .withWorkDirectory(workDirectory)
                .withCharset(Charsets.UTF_8)

            outputLevel.cliArg?.let { command.addParameter(it) }
            command.addParameters("analyzer", "--stdio")
            if (lint) {
                command.addParameter("--lint")
            }

            return command
        }
    }
}
