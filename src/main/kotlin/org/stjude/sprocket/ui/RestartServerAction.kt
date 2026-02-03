package org.stjude.sprocket.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.stjude.sprocket.server.SprocketServerManager

class RestartServerAction : AnAction("Restart Server", "Restart the Sprocket language server", null) {
    override fun actionPerformed(e: AnActionEvent) {
        SprocketServerManager.getInstance().restartServer()
    }
}
