package org.stjude.sprocket.lsp

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.OSProcessStreamConnectionProvider
import org.stjude.sprocket.cli.SprocketCommand
import org.stjude.sprocket.server.SprocketServerManager

class SprocketStreamConnectionProvider(private val project: Project) : OSProcessStreamConnectionProvider() {
    companion object {
        private val LOG = Logger.getInstance(SprocketStreamConnectionProvider::class.java)
    }

    override fun start() {
        val manager = SprocketServerManager.getInstance()
        val command = SprocketCommand(project).server()

        if (command != null) {
            LOG.info("Starting sprocket server with command: `${command}`")
            super.setCommandLine(command)
            super.start()
        } else {
            manager.notifyMissingBinary(project)
        }
    }
}
