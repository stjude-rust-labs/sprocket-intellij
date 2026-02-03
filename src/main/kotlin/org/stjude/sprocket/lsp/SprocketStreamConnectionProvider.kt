package org.stjude.sprocket.lsp

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.ProcessStreamConnectionProvider
import org.stjude.sprocket.server.SprocketServerManager

class SprocketStreamConnectionProvider(private val project: Project) : ProcessStreamConnectionProvider() {

    init {
        val manager = SprocketServerManager.getInstance()
        val command = manager.buildServerCommand()

        if (command != null) {
            super.setCommands(command)
        } else {
            manager.notifyMissingBinary(project)
        }
    }
}
