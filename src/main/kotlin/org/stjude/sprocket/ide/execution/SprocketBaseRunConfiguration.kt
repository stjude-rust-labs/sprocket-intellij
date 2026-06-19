package org.stjude.sprocket.ide.execution

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.openapi.project.Project
import org.jdom.Element
import org.stjude.sprocket.ide.ui.SprocketTestConfigurationEditor

/**
 * Base run configuration for `sprocket` commands.
 *
 * This includes the globally-applicable options for `sprocket`.
 */
abstract class SprocketBaseRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<RunProfileState>(project, factory, name) {

    var colorOption: ColorOption = ColorOption.AUTO
    var configPath: String = ""
    var skipConfigSearch: Boolean = false

    enum class ColorOption { AUTO, ALWAYS, NEVER }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        colorOption = ColorOption.valueOf(element.getAttributeValue("colorOption") ?: ColorOption.AUTO.name)
        configPath = element.getAttributeValue("configPath") ?: ""
        skipConfigSearch = element.getAttributeValue("skipConfigSearch").toBoolean()
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute("colorOption", colorOption.name)
        element.setAttribute("configPath", configPath)
        element.setAttribute("skipConfigSearch", skipConfigSearch.toString())
    }
}