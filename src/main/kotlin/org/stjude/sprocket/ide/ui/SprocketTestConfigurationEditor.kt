package org.stjude.sprocket.ide.ui

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import org.stjude.sprocket.ide.execution.test.SprocketTestRunConfiguration

class SprocketTestConfigurationEditor : SprocketBaseConfigurationEditor<SprocketTestRunConfiguration>() {

    private val sourceField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            "Select Source",
            "Select WDL document or workspace",
            null,
            FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()
        )
    }
    private val workspaceField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            "Select Workspace",
            "Root of the workspace",
            null,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        )
    }
    private val fixturesDirField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            "Select Fixtures Directory",
            null,
            null,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        )
    }
    private val runDirField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            "Select Run Directory",
            null,
            null,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        )
    }

    private val targetField = JBTextField()
    private val filterField = JBTextField()
    private val exactField = JBCheckBox("Exact match (do not print results as tests complete)")
    private val includeTagsField = JBTextField()
    private val excludeTagsField = JBTextField()
    private val parallelismField = JBTextField()

    private val cleanBehaviorBox = ComboBox(SprocketTestRunConfiguration.CleanBehavior.entries.toTypedArray())

    private val noStatusCheck = JBCheckBox("No status (do not print results as tests complete)")

    override fun Panel.buildUi() {
        group("Paths") {
            row("Source:") {
                cell(sourceField).align(AlignX.FILL).comment("Leave blank for current working directory")
            }
            row("Workspace (-w):") { cell(workspaceField).align(AlignX.FILL) }
            row("Fixtures Dir:") { cell(fixturesDirField).align(AlignX.FILL) }
            row("Run Dir:") { cell(runDirField).align(AlignX.FILL) }
        }
        group("Execution") {
            row("Target (-t):") { cell(targetField).align(AlignX.FILL) }
            row("Filter (-f):") { cell(filterField).align(AlignX.FILL).comment("Test name filter") }
            row("Exact match") { cell(exactField).align(AlignX.FILL) }
            row("Include Tags (-i):") {
                cell(includeTagsField).align(AlignX.FILL).comment("Comma-separated list of tags")
            }
            row("Exclude Tags (-e):") {
                cell(excludeTagsField).align(AlignX.FILL).comment("Comma-separated list of tags to exclude")
            }
            row("Parallelism (-p):") { cell(parallelismField) }
            row("Clean Behavior:") { cell(cleanBehaviorBox) }
        }
    }

    override fun resetEditorFrom(config: SprocketTestRunConfiguration) {
        super.resetEditorFrom(config)
        sourceField.text = config.sourcePath
        workspaceField.text = config.workspacePath
        targetField.text = config.target
        filterField.text = config.filter
        exactField.isSelected = config.exact
        includeTagsField.text = config.includeTags
        excludeTagsField.text = config.excludeTags
        cleanBehaviorBox.selectedItem = config.cleanBehavior
        parallelismField.text = config.parallelism
        fixturesDirField.text = config.fixturesDir
        runDirField.text = config.runDir
        noStatusCheck.isSelected = config.noStatus
    }

    override fun applyEditorTo(config: SprocketTestRunConfiguration) {
        super.applyEditorTo(config)
        config.sourcePath = sourceField.text
        config.workspacePath = workspaceField.text
        config.target = targetField.text
        config.filter = filterField.text
        config.exact = exactField.isSelected
        config.includeTags = includeTagsField.text
        config.excludeTags = excludeTagsField.text
        config.cleanBehavior = cleanBehaviorBox.selectedItem as SprocketTestRunConfiguration.CleanBehavior
        config.parallelism = parallelismField.text
        config.fixturesDir = fixturesDirField.text
        config.runDir = runDirField.text
        config.noStatus = noStatusCheck.isSelected
    }
}