package org.stjude.sprocket.cli

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import org.stjude.sprocket.ide.execution.SprocketBaseRunConfiguration
import org.stjude.sprocket.ide.execution.run.SprocketRunRunConfiguration
import org.stjude.sprocket.ide.execution.test.SprocketTestRunConfiguration
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

    fun test(config: SprocketTestRunConfiguration): GeneralCommandLine? {
        val binary = sprocketBinary ?: return null
        return testCommand(binary, projectSettings.outputLevel(), config, project.basePath)
    }

    fun run(config: SprocketRunRunConfiguration): GeneralCommandLine? {
        val binary = sprocketBinary ?: return null
        return runCommand(binary, projectSettings.outputLevel(), config, project.basePath)
    }

    companion object {
        /**
         * Builds the `sprocket analyzer --stdio` server command.
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

        /**
         * Builds the `sprocket dev test` command based on the Run Configuration UI state.
         */
        fun testCommand(
            binary: File,
            outputLevel: OutputLevel,
            config: SprocketTestRunConfiguration,
            defaultWorkDirectory: String?
        ): GeneralCommandLine {
            val command = GeneralCommandLine(binary.absolutePath)
                .withWorkDirectory(defaultWorkDirectory)
                .withCharset(Charsets.UTF_8)

            command.addParameters("dev", "test")

            applyBaseConfig(config, outputLevel, command)

            if (config.workspacePath.isNotBlank()) {
                command.addParameters("-w", config.workspacePath)
            }

            if (config.target.isNotBlank()) {
                command.addParameters("-t", config.target)
            }

            if (config.filter.isNotBlank()) {
                command.addParameters("-f", config.filter)
            }

            if (config.exact) {
                command.addParameter("--exact")
            }

            config.includeTags.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach {
                command.addParameters("-i", it)
            }
            config.excludeTags.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach {
                command.addParameters("-e", it)
            }

            when (config.cleanBehavior) {
                SprocketTestRunConfiguration.CleanBehavior.NO_CLEAN -> command.addParameter("--no-clean")
                SprocketTestRunConfiguration.CleanBehavior.CLEAN_ALL -> command.addParameter("--clean-all")
                SprocketTestRunConfiguration.CleanBehavior.DEFAULT -> {}
            }

            if (config.parallelism.isNotBlank()) {
                command.addParameters("-p", config.parallelism)
            }

            if (config.fixturesDir.isNotBlank()) command.addParameters("--fixtures-dir", config.fixturesDir)
            if (config.runDir.isNotBlank()) command.addParameters("--run-dir", config.runDir)
            if (config.noStatus) command.addParameter("--no-status")

            if (config.sourcePath.isNotBlank()) {
                command.addParameter(config.sourcePath)
            }

            return command
        }

        /**
         * Builds the `sprocket run` command based on the Run Configuration UI state.
         */
        fun runCommand(
            binary: File,
            outputLevel: OutputLevel,
            config: SprocketRunRunConfiguration,
            defaultWorkDirectory: String?
        ): GeneralCommandLine {
            val command = GeneralCommandLine(binary.absolutePath)
                .withWorkDirectory(defaultWorkDirectory)
                .withCharset(Charsets.UTF_8)

            command.addParameter("run")

            applyBaseConfig(config, outputLevel, command)

            if (config.target.isNotBlank()) {
                command.addParameters("-t", config.target)
            }

            if (config.sourcePath.isNotBlank()) {
                command.addParameter(config.sourcePath)
            }

            return command
        }

        private fun applyBaseConfig(
            config: SprocketBaseRunConfiguration,
            outputLevel: OutputLevel,
            command: GeneralCommandLine
        ) {
            outputLevel.cliArg?.let { command.addParameter(it) }

            if (config.colorOption != SprocketBaseRunConfiguration.ColorOption.AUTO) {
                command.addParameters("--color", config.colorOption.name.lowercase())
            }
            if (config.configPath.isNotBlank()) {
                command.addParameters("-c", config.configPath)
            }
            if (config.skipConfigSearch) {
                command.addParameter("-s")
            }
        }
    }
}
