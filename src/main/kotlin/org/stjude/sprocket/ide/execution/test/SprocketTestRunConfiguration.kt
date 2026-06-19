package org.stjude.sprocket.ide.execution.test

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import org.jdom.Element
import org.stjude.sprocket.ide.execution.SprocketBaseRunConfiguration
import org.stjude.sprocket.ide.ui.SprocketTestConfigurationEditor

/**
 * Run configuration for `sprocket dev test` commands.
 */
class SprocketTestRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : SprocketBaseRunConfiguration(project, factory, name) {

    var sourcePath: String = ""
    var workspacePath: String = ""
    var target: String = ""
    var filter: String = ""
    var exact: Boolean = false
    var includeTags: String = ""
    var excludeTags: String = ""
    var cleanBehavior: CleanBehavior = CleanBehavior.DEFAULT
    var parallelism: String = ""
    var fixturesDir: String = ""
    var runDir: String = ""
    var noStatus: Boolean = false

    enum class CleanBehavior { DEFAULT, NO_CLEAN, CLEAN_ALL }

    override fun getConfigurationEditor() = SprocketTestConfigurationEditor()

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return SprocketTestRunProfileState(this, environment)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        sourcePath = element.getAttributeValue("sourcePath") ?: ""
        workspacePath = element.getAttributeValue("workspacePath") ?: ""
        target = element.getAttributeValue("target") ?: ""
        filter = element.getAttributeValue("filter") ?: ""
        exact = element.getAttributeValue("exact").toBoolean()
        includeTags = element.getAttributeValue("includeTags") ?: ""
        excludeTags = element.getAttributeValue("excludeTags") ?: ""
        cleanBehavior = CleanBehavior.valueOf(element.getAttributeValue("cleanBehavior") ?: CleanBehavior.DEFAULT.name)
        parallelism = element.getAttributeValue("parallelism") ?: ""
        fixturesDir = element.getAttributeValue("fixturesDir") ?: ""
        runDir = element.getAttributeValue("runDir") ?: ""
        noStatus = element.getAttributeValue("noStatus").toBoolean()
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute("sourcePath", sourcePath)
        element.setAttribute("workspacePath", workspacePath)
        element.setAttribute("target", target)
        element.setAttribute("filter", filter)
        element.setAttribute("exact", exact.toString())
        element.setAttribute("includeTags", includeTags)
        element.setAttribute("excludeTags", excludeTags)
        element.setAttribute("cleanBehavior", cleanBehavior.name)
        element.setAttribute("parallelism", parallelism)
        element.setAttribute("fixturesDir", fixturesDir)
        element.setAttribute("runDir", runDir)
        element.setAttribute("noStatus", noStatus.toString())
    }
}