package org.stjude.sprocket.ide.execution.run

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.runners.ExecutionEnvironment
import org.stjude.sprocket.cli.SprocketCommand
import com.intellij.execution.ExecutionException
import org.stjude.sprocket.server.SprocketServerManager

class SprocketRunRunProfileState(
    private val config: SprocketRunRunConfiguration,
    environment: ExecutionEnvironment
) : CommandLineState(environment) {

    @Throws(ExecutionException::class)
    override fun startProcess(): ProcessHandler {
        val manager = SprocketServerManager.getInstance()
        val sprocketCmd = SprocketCommand(config.project)

        val commandLine = sprocketCmd.run(config)
        if (commandLine == null) {
            manager.notifyMissingBinary(config.project)
            throw ExecutionException("Sprocket binary could not be resolved")
        }

        return ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
    }
}