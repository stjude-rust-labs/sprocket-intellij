package org.stjude.sprocket.lsp

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.OSProcessStreamConnectionProvider
import org.stjude.sprocket.server.SprocketServerManager

class SprocketStreamConnectionProvider(private val project: Project) : OSProcessStreamConnectionProvider() {

    override fun start() {
        val manager = SprocketServerManager.getInstance()
        val command = manager.buildServerCommand(project)

        if (command != null) {
            super.setCommandLine(command)
        } else {
            manager.notifyMissingBinary(project)
        }

        super.start()
    }
}
