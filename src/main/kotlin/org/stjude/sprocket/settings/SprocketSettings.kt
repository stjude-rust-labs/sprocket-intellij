package org.stjude.sprocket.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.APP)
@State(
    name = "org.stjude.sprocket.settings.SprocketSettings",
    storages = [Storage("sprocket.xml")]
)
class SprocketSettings : PersistentStateComponent<SprocketSettings> {
    var checkForUpdates: Boolean = true
    var binaryPath: String = ""
    var outputLevel: OutputLevel = OutputLevel.QUIET
    var lint: Boolean = false
    var maxRetries: Int = 1

    override fun getState(): SprocketSettings = this

    override fun loadState(state: SprocketSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): SprocketSettings =
            ApplicationManager.getApplication().getService(SprocketSettings::class.java)
    }
}
