package org.stjude.sprocket.cli

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import org.stjude.sprocket.server.SprocketServerManager
import org.stjude.sprocket.settings.SprocketSettings

/**
 * Utilities for building `GeneralCommandLine`s for the `sprocket` binary.
 */
class SprocketCommand(val project: Project) {
    val projectSettings = SprocketSettings.getInstance(project)
    val sprocketBinary = SprocketServerManager.getInstance().resolveBinary(project)

    private fun baseCommand(): GeneralCommandLine? {
        if (sprocketBinary == null) {
            return null
        }

        val cmd = GeneralCommandLine(sprocketBinary.path)
            .withWorkDirectory(project.basePath)
            .withCharset(Charsets.UTF_8)

        projectSettings.outputLevel().cliArg?.let { cmd.addParameter(it) }

        return cmd
    }

    fun server(): GeneralCommandLine? {
        val serverArgs = mutableListOf("analyzer", "--stdio")

        if (projectSettings.lint()) {
            serverArgs.add("--lint")
        }

        return baseCommand()?.withParameters(serverArgs)
    }
}