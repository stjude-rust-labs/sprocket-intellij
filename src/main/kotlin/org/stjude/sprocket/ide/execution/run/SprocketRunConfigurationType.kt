package org.stjude.sprocket.ide.execution.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.SimpleConfigurationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import org.stjude.sprocket.WdlIcons

/**
 * The `sprocket run` run configuration type.
 *
 * @see SprocketRunRunConfiguration
 */
class SprocketRunConfigurationType : SimpleConfigurationType(
    "SprocketRunRunConfiguration",
    "Sprocket Run",
    "Run a WDL workflow/task using sprocket",
    NotNullLazyValue.createValue { WdlIcons.FILE }
) {
    val factory: ConfigurationFactory get() = configurationFactories.single()

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return SprocketRunRunConfiguration(project, this, "Sprocket Run")
    }

    companion object {
        fun getInstance(): SprocketRunConfigurationType =
            ConfigurationTypeUtil.findConfigurationType(SprocketRunConfigurationType::class.java)
    }
}
