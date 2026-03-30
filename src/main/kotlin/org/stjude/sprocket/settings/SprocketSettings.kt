package org.stjude.sprocket.settings

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.containers.ContainerUtil

@Service(Service.Level.PROJECT)
@State(
    name = "org.stjude.sprocket.settings.SprocketSettings",
    storages = [Storage("sprocket.xml")]
)
class SprocketSettings : PersistentStateComponent<SprocketSettings.State> {
    var theState = State()
    private val changeHandlers = ContainerUtil.createConcurrentList<Runnable>()

    override fun getState(): State = this.theState

    override fun loadState(state: State) {
        this.theState = state
    }

    fun binaryPath(): String = this.state.binaryPath

    fun outputLevel(): OutputLevel = this.state.options.outputLevel

    fun lint(): Boolean = this.state.options.lintOptions.enabled

    fun addChangeHandler(runnable: Runnable) = changeHandlers.add(runnable)

    fun removeChangeHandler(runnable: Runnable) = changeHandlers.remove(runnable)

    fun fireStateChanged() {
        for (runnable in changeHandlers) {
            runnable.run()
        }
    }

    fun toLSPSettings(): JsonObject {
        val settings = JsonObject()
        settings.add("sprocket.server", state.options.toLSPSettings())
        return settings
    }

    data class State(
        var binaryPath: String = "",
        var options: ServerOptions = ServerOptions()
    )

    data class ServerOptions(
        var outputLevel: OutputLevel = OutputLevel.QUIET,
        var lintOptions: LintOptions = LintOptions()
    ) {
        fun toLSPSettings(): JsonObject {
            val settings = JsonObject()

            settings.add("logLevel", JsonPrimitive(outputLevel.levelFilter))
            settings.add("lint",lintOptions.toLSPSettings())

            return settings
        }
    }

    data class LintOptions(
        var enabled: Boolean = false,
    ) {
        fun toLSPSettings(): JsonObject {
            val settings = JsonObject()

            settings.add("enabled", JsonPrimitive(enabled))

            return settings
        }
    }

    companion object {
        fun getInstance(project: Project): SprocketSettings =
            project.getService(SprocketSettings::class.java)
    }
}
