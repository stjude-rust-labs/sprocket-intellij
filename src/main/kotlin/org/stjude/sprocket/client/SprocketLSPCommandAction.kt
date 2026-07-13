package org.stjude.sprocket.client

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.util.concurrency.AppExecutorUtil
import com.redhat.devtools.lsp4ij.commands.LSPCommand
import com.redhat.devtools.lsp4ij.commands.LSPCommandAction
import org.stjude.sprocket.ide.execution.run.SprocketRunRunConfiguration
import org.stjude.sprocket.ide.execution.run.isCallableIdentifier

/**
 * Handlers for server-defined commands.
 */
class SprocketLSPCommandAction : LSPCommandAction() {
    private val log = thisLogger()

    override fun commandPerformed(
        command: LSPCommand,
        e: AnActionEvent,
    ) {
        log.info("Received a command: ${command.command} (title=${command.title})")

        val project = e.project ?: return

        when (command.command) {
            "sprocket.run" -> {
                executeRunConfiguration(project, command)
            }

            else -> {
                log.warn("Unknown command: ${command.command}")
                return
            }
        }
    }

    private fun executeRunConfiguration(
        project: Project,
        command: LSPCommand,
    ) {
        val sourcePathArg = command.getArgumentAt(0, String::class.java)
        val targetArg = command.getArgumentAt(1, String::class.java)

        if (sourcePathArg == null || targetArg == null) {
            log.error("Received a bad `run` command (args=${command.arguments})")
            return
        }

        ReadAction
            .nonBlocking<Pair<RunManager, RunnerAndConfigurationSettings>?> {
                // Dance between the LSP and PSI, trying to find the definition of the target.
                //
                // Necessary since the `RunConfigurationProducer` handles duplicate checks against `PsiElement`s.
                val targetElement = findTargetPsiElement(project, sourcePathArg, targetArg) ?: return@nonBlocking null

                val context = ConfigurationContext(targetElement)
                val runManager = RunManager.getInstance(project)

                var settings = context.findExisting()
                if (settings == null || settings.configuration !is SprocketRunRunConfiguration) {
                    settings = context.configuration ?: return@nonBlocking null
                    settings.name = "Run `$targetArg`"

                    val config = settings.configuration as SprocketRunRunConfiguration
                    config.sourcePath = sourcePathArg
                    config.target = targetArg

                    runManager.addConfiguration(settings)
                }

                Pair(runManager, settings)
            }.finishOnUiThread(ModalityState.defaultModalityState()) { pair ->
                if (pair == null) {
                    log.error("Could not resolve run configuration for target: $targetArg")
                    return@finishOnUiThread
                }

                val (runManager, settings) = pair
                runManager.selectedConfiguration = settings

                val executor = DefaultRunExecutor.getRunExecutorInstance()
                ProgramRunnerUtil.executeConfiguration(settings, executor)
            }.submit(AppExecutorUtil.getAppExecutorService())
    }

    /**
     * Find the `PsiElement` that defines a `sprocket run` target.
     */
    private fun findTargetPsiElement(
        project: Project,
        sourceUrl: String,
        targetName: String,
    ): PsiElement? {
        val virtualFile = VirtualFileManager.getInstance().findFileByUrl(sourceUrl) ?: return null
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return null

        var result: PsiElement? = null
        psiFile.accept(
            object : PsiRecursiveElementWalkingVisitor() {
                override fun visitElement(element: PsiElement) {
                    if (element.text == targetName && element.isCallableIdentifier()) {
                        result = element
                        stopWalking()
                    }
                    super.visitElement(element)
                }
            },
        )
        return result
    }
}
