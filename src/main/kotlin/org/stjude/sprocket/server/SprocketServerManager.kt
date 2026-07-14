package org.stjude.sprocket.server

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.EnvironmentUtil
import org.stjude.sprocket.settings.SprocketSettings
import java.io.File

@Service(Service.Level.APP)
class SprocketServerManager {
    @Volatile
    private var notifiedMissingBinary = false

    companion object {
        private val LOG = Logger.getInstance(SprocketServerManager::class.java)

        private val COMMON_PATHS: List<String> by lazy {
            val home = System.getProperty("user.home")
            val isWindows = System.getProperty("os.name").lowercase().contains("windows")

            if (isWindows) {
                listOf("$home\\.cargo\\bin\\sprocket.exe")
            } else {
                listOf(
                    "$home/.cargo/bin/sprocket",
                    "/opt/homebrew/bin/sprocket",
                    "/usr/local/bin/sprocket",
                )
            }
        }

        fun getInstance(): SprocketServerManager = ApplicationManager.getApplication().getService(SprocketServerManager::class.java)
    }

    fun notifyMissingBinary(project: Project) {
        if (notifiedMissingBinary) return
        notifiedMissingBinary = true

        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Sprocket")
            .createNotification(
                "Sprocket not found",
                "The sprocket binary was not found. Please install sprocket and configure the path in Settings → Tools → Sprocket.",
                NotificationType.ERROR,
            ).notify(project)
    }

    fun resolveBinary(project: Project): File? {
        val settings = SprocketSettings.getInstance(project)

        if (settings.binaryPath().isNotBlank()) {
            val manual = File(settings.binaryPath())
            if (manual.exists() && manual.canExecute()) {
                return manual
            }
            LOG.warn("Configured binary path does not exist or is not executable: ${settings.binaryPath()}")
        }

        findInPath("sprocket")?.let { return it }

        for (path in COMMON_PATHS) {
            val file = File(path)
            if (file.exists() && file.canExecute()) {
                return file
            }
        }

        return null
    }

    private fun findInPath(name: String): File? {
        val pathEnv = EnvironmentUtil.getValue("PATH") ?: System.getenv("PATH") ?: return null

        return pathEnv
            .split(File.pathSeparator)
            .map { File(it, name) }
            .firstOrNull { it.exists() && it.canExecute() }
    }
}
