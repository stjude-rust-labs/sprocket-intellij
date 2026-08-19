package org.stjude.sprocket.ide.execution.test

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.SimpleConfigurationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import org.stjude.sprocket.WdlIcons

/**
 * The `sprocket dev test` run configuration type.
 *
 * @see SprocketTestRunConfiguration
 */
class SprocketTestConfigurationType : SimpleConfigurationType(
    "SprocketTestRunConfiguration",
    "Sprocket Test",
    "Run unit tests for a WDL workspace using sprocket",
    NotNullLazyValue.createValue { WdlIcons.FILE }
) {
    val factory: ConfigurationFactory get() = configurationFactories.single()

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return SprocketTestRunConfiguration(project, this, "Sprocket Test")
    }

    companion object {
        fun getInstance(): SprocketTestConfigurationType =
            ConfigurationTypeUtil.findConfigurationType(SprocketTestConfigurationType::class.java)
    }
}
