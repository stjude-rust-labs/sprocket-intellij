package org.stjude.sprocket.client

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import org.eclipse.lsp4j.RegistrationParams
import org.eclipse.lsp4j.UnregistrationParams
import org.stjude.sprocket.settings.SprocketSettings
import java.util.concurrent.CompletableFuture

class WdlLanguageClient(
    project: Project,
) : LanguageClientImpl(project) {
    @Volatile
    private var didChangeConfigurationSupported = false

    private val maybeDidChangeConfigurationListener =
        Runnable {
            if (didChangeConfigurationSupported) {
                didChangeConfigurationListener.run()
            }
        }

    init {
        SprocketSettings.getInstance(project).addChangeHandler(maybeDidChangeConfigurationListener)
    }

    override fun registerCapability(params: RegistrationParams): CompletableFuture<Void> {
        params.registrations.forEach { registration ->
            if (registration.method == "workspace/didChangeConfiguration") {
                didChangeConfigurationSupported = true
            }
        }
        return super.registerCapability(params)
    }

    override fun unregisterCapability(params: UnregistrationParams): CompletableFuture<Void> {
        params.unregisterations.forEach { unregistration ->
            if (unregistration.method == "workspace/didChangeConfiguration") {
                didChangeConfigurationSupported = false
            }
        }
        return super.unregisterCapability(params)
    }

    override fun createSettings(): Any = SprocketSettings.getInstance(project).toLSPSettings()

    override fun dispose() {
        super.dispose()
        SprocketSettings.getInstance(project).removeChangeHandler(maybeDidChangeConfigurationListener)
    }
}
