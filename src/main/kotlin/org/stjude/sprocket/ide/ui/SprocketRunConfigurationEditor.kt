package org.stjude.sprocket.ide.ui

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Panel
import org.stjude.sprocket.ide.execution.run.SprocketRunRunConfiguration

class SprocketRunConfigurationEditor : SprocketBaseConfigurationEditor<SprocketRunRunConfiguration>() {

    private val sourceField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            "Select Source",
            "Select WDL document or workspace",
            null,
            FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()
        )
    }

    private val targetField = JBTextField()

    override fun Panel.buildUi() {
        group("Execution") {
            row("Target (-t):") {
                cell(targetField).align(AlignX.FILL).comment("The workflow/task to run")
            }
        }
    }

    override fun resetEditorFrom(config: SprocketRunRunConfiguration) {
        super.resetEditorFrom(config)
        sourceField.text = config.sourcePath
        targetField.text = config.target
    }

    override fun applyEditorTo(config: SprocketRunRunConfiguration) {
        super.applyEditorTo(config)
        config.sourcePath = sourceField.text
        config.target = targetField.text
    }
}