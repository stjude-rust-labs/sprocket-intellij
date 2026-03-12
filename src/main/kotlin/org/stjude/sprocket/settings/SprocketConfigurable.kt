package org.stjude.sprocket.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.*

class SprocketConfigurable(private val project: Project) : BoundConfigurable("Sprocket") {
    private val settings get() = SprocketSettings.getInstance(project)

    override fun apply() {
        super.apply()
        settings.fireStateChanged()
    }

    override fun createPanel() = panel {
        row("Sprocket binary path:") {
            textFieldWithBrowseButton(
                "Select Sprocket Binary",
                project,
                FileChooserDescriptorFactory.createSingleFileDescriptor()
            )
                .align(AlignX.FILL)
                .bindText(settings.state::binaryPath)
                .comment("Leave empty to use PATH lookup")
        }
        row("Output level:") {
            comboBox(OutputLevel.entries)
                .bindItem(settings.state.options::outputLevel.toNullableProperty())
        }
        row {
            checkBox("Enable lint checks")
                .bindSelected(settings.state.options.lintOptions::enabled)
        }
    }
}
