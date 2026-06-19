package org.stjude.sprocket.client

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.commands.LSPCommand
import com.redhat.devtools.lsp4ij.commands.LSPCommandAction
import org.stjude.sprocket.ide.execution.run.SprocketRunConfigurationType
import org.stjude.sprocket.ide.execution.run.SprocketRunRunConfiguration
import org.stjude.sprocket.ide.execution.test.SprocketTestConfigurationType
import org.stjude.sprocket.ide.execution.test.SprocketTestRunConfiguration

/**
 * Handlers for server-defined commands.
 */
class SprocketLSPCommandAction : LSPCommandAction() {
    private val log = thisLogger();

    override fun commandPerformed(
        command: LSPCommand,
        e: AnActionEvent
    ) {
        log.info("Received a command: ${command.command} (title=${command.title})")

        val project = e.project ?: return

        when (command.command) {
            "sprocket.testTarget" -> {
                ApplicationManager.getApplication().invokeLater {
                    executeTestConfiguration(project, command, false)
                }
            }
            "sprocket.testSingle" -> {
                ApplicationManager.getApplication().invokeLater {
                    executeTestConfiguration(project, command, true)
                }
            }

            "sprocket.run" -> {
                ApplicationManager.getApplication().invokeLater {
                    executeRunConfiguration(project, command)
                }
            }

            else -> {
                log.warn("Unknown command: ${command.command}")
                return
            }
        }
    }

    private fun executeTestConfiguration(project: Project, command: LSPCommand, isTestSingle: Boolean) {
        val sourcePathArg = command.getArgumentAt(0, String::class.java)
        val targetArg = command.getArgumentAt(1, String::class.java)

        var filterArg: String? = null
        if (isTestSingle) {
            filterArg = command.getArgumentAt(2, String::class.java)
        }

        if (sourcePathArg == null || targetArg == null) {
            log.error("Received a bad `test` command (args=${command.arguments})")
            return
        }

        val runManager = RunManager.getInstance(project)
        val factory = SprocketTestConfigurationType.getInstance().factory

        val settings: RunnerAndConfigurationSettings = runManager.createConfiguration("Test `$targetArg`", factory)
        settings.isTemporary = true

        val config = settings.configuration as SprocketTestRunConfiguration
        config.sourcePath = sourcePathArg
        config.target = targetArg
        if (filterArg != null) {
            config.filter = filterArg
        }
        config.exact = true

        runManager.addConfiguration(settings)
        runManager.selectedConfiguration = settings

        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ProgramRunnerUtil.executeConfiguration(settings, executor)
    }

    private fun executeRunConfiguration(project: Project, command: LSPCommand) {
        val sourcePathArg = command.getArgumentAt(0, String::class.java)
        val targetArg = command.getArgumentAt(1, String::class.java)

        if (sourcePathArg == null || targetArg == null) {
            log.error("Received a bad `run` command (args=${command.arguments})")
            return
        }

        val runManager = RunManager.getInstance(project)
        val factory = SprocketRunConfigurationType.getInstance().factory

        val settings: RunnerAndConfigurationSettings = runManager.createConfiguration("Run `$targetArg`", factory)
        settings.isTemporary = true

        val config = settings.configuration as SprocketRunRunConfiguration
        config.sourcePath = sourcePathArg
        config.target = targetArg

        runManager.addConfiguration(settings)
        runManager.selectedConfiguration = settings

        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ProgramRunnerUtil.executeConfiguration(settings, executor)
    }
}