package org.stjude.sprocket

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.stjude.sprocket.server.SprocketServerManager

class SprocketStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val manager = SprocketServerManager.getInstance()
        if (manager.resolveBinary() == null) {
            manager.notifyMissingBinary(project)
        }
    }
}
