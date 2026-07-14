package org.stjude.sprocket.ide.ui

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import org.stjude.sprocket.ide.execution.SprocketBaseRunConfiguration
import javax.swing.JComponent

abstract class SprocketBaseConfigurationEditor<T : SprocketBaseRunConfiguration> : SettingsEditor<T>() {
    private val configPathField =
        TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                "Select Config File",
                null,
                null,
                FileChooserDescriptorFactory.createSingleFileDescriptor(),
            )
        }

    private val colorBox = ComboBox(SprocketBaseRunConfiguration.ColorOption.entries.toTypedArray())
    private val skipConfigSearchCheck = JBCheckBox("Skip config search (-s)")

    override fun createEditor(): JComponent =
        panel {
            buildUi()
            group("Output") {
                row("Color:") { cell(colorBox) }
                row("Config File (-c):") { cell(configPathField).align(AlignX.FILL) }
                row { cell(skipConfigSearchCheck) }
            }
        }

    /**
     * Add the command-specific UI elements to the panel.
     */
    protected abstract fun Panel.buildUi()

    override fun resetEditorFrom(config: T) {
        colorBox.selectedItem = config.colorOption
        configPathField.text = config.configPath
        skipConfigSearchCheck.isSelected = config.skipConfigSearch
    }

    override fun applyEditorTo(config: T) {
        config.colorOption = colorBox.selectedItem as SprocketBaseRunConfiguration.ColorOption
        config.configPath = configPathField.text
        config.skipConfigSearch = skipConfigSearchCheck.isSelected
    }
}
