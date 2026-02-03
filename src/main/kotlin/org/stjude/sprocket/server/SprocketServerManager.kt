package org.stjude.sprocket.server

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import org.stjude.sprocket.settings.SprocketSettings
import com.intellij.util.EnvironmentUtil
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

@Service(Service.Level.APP)
class SprocketServerManager : Disposable {
    private val listeners = CopyOnWriteArrayList<(ServerState) -> Unit>()

    @Volatile
    private var notifiedMissingBinary = false

    @Volatile
    var state: ServerState = ServerState.STOPPED
        private set(value) {
            field = value
            listeners.forEach { it(value) }
        }

    @Volatile
    var serverProcess: Process? = null
        private set

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
                    "/usr/local/bin/sprocket"
                )
            }
        }

        fun getInstance(): SprocketServerManager =
            ApplicationManager.getApplication().getService(SprocketServerManager::class.java)
    }

    fun addStateListener(listener: (ServerState) -> Unit) {
        listeners.add(listener)
    }

    fun removeStateListener(listener: (ServerState) -> Unit) {
        listeners.remove(listener)
    }

    fun shouldNotifyMissingBinary(): Boolean {
        if (notifiedMissingBinary) return false
        notifiedMissingBinary = true
        return true
    }

    fun notifyMissingBinary(project: Project) {
        if (!shouldNotifyMissingBinary()) return

        NotificationGroupManager.getInstance()
            .getNotificationGroup("Sprocket")
            .createNotification(
                "Sprocket not found",
                "The sprocket binary was not found. " +
                    "Please install sprocket and configure the path in " +
                    "Settings → Tools → Sprocket.",
                NotificationType.ERROR
            )
            .notify(project)
    }

    fun resolveBinary(): File? {
        val settings = SprocketSettings.getInstance()

        if (settings.binaryPath.isNotBlank()) {
            val manual = File(settings.binaryPath)
            if (manual.exists() && manual.canExecute()) {
                return manual
            }
            LOG.warn("Configured binary path does not exist or is not executable: ${settings.binaryPath}")
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

        return pathEnv.split(File.pathSeparator)
            .map { File(it, name) }
            .firstOrNull { it.exists() && it.canExecute() }
    }

    fun buildServerCommand(): List<String>? {
        val binary = resolveBinary() ?: return null

        val settings = SprocketSettings.getInstance()
        val command = mutableListOf(binary.absolutePath, "analyzer", "--stdio")

        settings.outputLevel.cliArg?.let { command.add(it) }
        if (settings.lint) {
            command.add("--lint")
        }

        return command
    }

    fun startServer(): Process? {
        val command = buildServerCommand()
        if (command == null) {
            state = ServerState.ERROR
            return null
        }

        state = ServerState.STARTING

        return try {
            val processBuilder = ProcessBuilder(command)
                .redirectErrorStream(false)

            val process = processBuilder.start()
            serverProcess = process
            state = ServerState.RUNNING

            Thread {
                try {
                    process.waitFor()
                    if (state == ServerState.RUNNING) {
                        state = ServerState.STOPPED
                    }
                } catch (_: InterruptedException) {
                    // Expected on shutdown
                }
            }.start()

            process
        } catch (e: Exception) {
            LOG.error("Failed to start Sprocket server", e)
            state = ServerState.ERROR
            null
        }
    }

    fun stopServer() {
        serverProcess?.let { process ->
            process.destroy()
            if (process.isAlive) {
                process.destroyForcibly()
            }
        }
        serverProcess = null
        state = ServerState.STOPPED
    }

    fun restartServer(): Process? {
        stopServer()
        return startServer()
    }

    override fun dispose() {
        stopServer()
        listeners.clear()
    }
}
