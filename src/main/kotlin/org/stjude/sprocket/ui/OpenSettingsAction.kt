package org.stjude.sprocket.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import org.stjude.sprocket.settings.SprocketConfigurable

class OpenSettingsAction : AnAction("Open Settings", "Open Sprocket settings", null) {
    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(
            e.project,
            SprocketConfigurable::class.java,
        )
    }
}
