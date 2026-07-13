package org.stjude.sprocket.ide.execution.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import org.jdom.Element
import org.stjude.sprocket.ide.execution.SprocketBaseRunConfiguration
import org.stjude.sprocket.ide.ui.SprocketRunConfigurationEditor

/**
 * Run configuration for `sprocket run` commands.
 */
class SprocketRunRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : SprocketBaseRunConfiguration(project, factory, name) {
    var sourcePath: String = ""
    var target: String = ""
    var outputDir: String = ""
    var suffix: String = ""
    var showStderr: Boolean = false

    override fun getConfigurationEditor() = SprocketRunConfigurationEditor()

    override fun getState(
        executor: Executor,
        environment: ExecutionEnvironment,
    ): RunProfileState = SprocketRunRunProfileState(this, environment)

    override fun readExternal(element: Element) {
        super.readExternal(element)
        sourcePath = element.getAttributeValue("sourcePath") ?: ""
        target = element.getAttributeValue("target") ?: ""
        outputDir = element.getAttributeValue("outputDir") ?: ""
        suffix = element.getAttributeValue("suffix") ?: ""
        showStderr = element.getAttributeValue("showStderr").toBoolean()
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute("sourcePath", sourcePath)
        element.setAttribute("target", target)
        element.setAttribute("outputDir", outputDir)
        element.setAttribute("suffix", suffix)
        element.setAttribute("showStderr", showStderr.toString())
    }
}
