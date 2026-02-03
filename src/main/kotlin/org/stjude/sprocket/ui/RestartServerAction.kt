package org.stjude.sprocket.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.redhat.devtools.lsp4ij.LanguageServerManager

class RestartServerAction : AnAction("Restart Server", "Restart the Sprocket language server", null) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        try {
            LanguageServerManager.getInstance(project).stop("sprocketLanguageServer")
            LanguageServerManager.getInstance(project).start("sprocketLanguageServer")
        } catch (_: Exception) {
            // Server restart failed, but don't crash
        }
    }
}
