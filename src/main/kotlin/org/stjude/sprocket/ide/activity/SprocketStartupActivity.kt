package org.stjude.sprocket.ide.activity

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.stjude.sprocket.server.SprocketServerManager

class SprocketStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val manager = SprocketServerManager.getInstance()
        if (manager.resolveBinary(project) == null) {
            manager.notifyMissingBinary(project)
        }
    }
}
