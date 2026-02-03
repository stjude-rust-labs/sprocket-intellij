package org.stjude.sprocket.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toNullableProperty
import javax.swing.JComponent

class SprocketConfigurable : Configurable {
    private val settings = SprocketSettings.getInstance()

    private var checkForUpdates = settings.checkForUpdates
    private var binaryPath = settings.binaryPath
    private var outputLevel = settings.outputLevel
    private var lint = settings.lint
    private var maxRetries = settings.maxRetries

    override fun getDisplayName(): String = "Sprocket"

    override fun createComponent(): JComponent = panel {
        row {
            checkBox("Check for updates on startup")
                .bindSelected(::checkForUpdates)
        }
        row("Sprocket binary path:") {
            textFieldWithBrowseButton(
                "Select Sprocket Binary",
                null,
                FileChooserDescriptorFactory.createSingleFileDescriptor()
            )
                .align(AlignX.FILL)
                .bindText(::binaryPath)
                .comment("Leave empty to download automatically")
        }
        row("Output level:") {
            comboBox(OutputLevel.entries)
                .bindItem(::outputLevel.toNullableProperty())
        }
        row {
            checkBox("Enable lint checks")
                .bindSelected(::lint)
        }
        row("Maximum retry attempts:") {
            spinner(0..10, 1)
                .bindIntValue(::maxRetries)
        }
    }

    override fun isModified(): Boolean =
        checkForUpdates != settings.checkForUpdates ||
        binaryPath != settings.binaryPath ||
        outputLevel != settings.outputLevel ||
        lint != settings.lint ||
        maxRetries != settings.maxRetries

    override fun apply() {
        settings.checkForUpdates = checkForUpdates
        settings.binaryPath = binaryPath
        settings.outputLevel = outputLevel
        settings.lint = lint
        settings.maxRetries = maxRetries
    }

    override fun reset() {
        checkForUpdates = settings.checkForUpdates
        binaryPath = settings.binaryPath
        outputLevel = settings.outputLevel
        lint = settings.lint
        maxRetries = settings.maxRetries
    }
}
