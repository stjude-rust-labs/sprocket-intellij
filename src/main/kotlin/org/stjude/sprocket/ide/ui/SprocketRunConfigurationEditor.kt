package org.stjude.sprocket.ide.ui

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Panel
import org.stjude.sprocket.ide.execution.run.SprocketRunRunConfiguration

/**
 * Configuration editor for `sprocket run` commands.
 *
 * @see SprocketRunRunConfiguration
 */
class SprocketRunConfigurationEditor : SprocketBaseConfigurationEditor<SprocketRunRunConfiguration>() {
    private val sourceField =
        TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                "Select Source",
                "Select WDL document or workspace",
                null,
                FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor(),
            )
        }

    private val outputDirField =
        TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                "Select Output Dir",
                "Select output directory",
                null,
                FileChooserDescriptorFactory.createSingleFolderDescriptor(),
            )
        }

    private val targetField = JBTextField()
    private val suffixField = JBTextField()
    private val showStderrField = JBCheckBox("Show task stderr")

    override fun Panel.buildUi() {
        group("Paths") {
            row("Source:") {
                cell(sourceField).align(AlignX.FILL).comment("Leave blank for current working directory")
            }
        }
        group("Execution") {
            row("Target (-t):") {
                cell(targetField).align(AlignX.FILL).comment("The workflow/task to run")
            }
            row("Output dir (-o):") {
                cell(outputDirField).align(AlignX.FILL).comment("Defaults to `./out`")
            }
            row("Suffix:") {
                cell(suffixField).align(AlignX.FILL).comment("Optional suffix to append to the run directory name")
            }
            row { cell(showStderrField) }
        }
    }

    override fun resetEditorFrom(config: SprocketRunRunConfiguration) {
        super.resetEditorFrom(config)
        sourceField.text = config.sourcePath
        targetField.text = config.target
        outputDirField.text = config.outputDir
        suffixField.text = config.suffix
        showStderrField.isSelected = config.showStderr
    }

    override fun applyEditorTo(config: SprocketRunRunConfiguration) {
        super.applyEditorTo(config)
        config.sourcePath = sourceField.text
        config.target = targetField.text
        config.outputDir = outputDirField.text
        config.suffix = suffixField.text
        config.showStderr = showStderrField.isSelected
    }
}
