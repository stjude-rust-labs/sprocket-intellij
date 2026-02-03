package org.stjude.sprocket.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toNullableProperty
import com.redhat.devtools.lsp4ij.LanguageServerManager

class SprocketConfigurable : BoundConfigurable("Sprocket") {
    private val settings = SprocketSettings.getInstance()

    override fun apply() {
        super.apply()
        for (project in ProjectManager.getInstance().openProjects) {
            try {
                LanguageServerManager.getInstance(project).stop("sprocketLanguageServer")
                LanguageServerManager.getInstance(project).start("sprocketLanguageServer")
            } catch (e: Exception) {
                // Server restart failed, but don't crash
            }
        }
    }

    override fun createPanel() = panel {
        row("Sprocket binary path:") {
            textFieldWithBrowseButton(
                "Select Sprocket Binary",
                null,
                FileChooserDescriptorFactory.createSingleFileDescriptor()
            )
                .align(AlignX.FILL)
                .bindText(settings::binaryPath)
                .comment("Leave empty to use PATH lookup")
        }
        row("Output level:") {
            comboBox(OutputLevel.entries)
                .bindItem(settings::outputLevel.toNullableProperty())
        }
        row {
            checkBox("Enable lint checks")
                .bindSelected(settings::lint)
        }
    }
}
