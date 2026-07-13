package org.stjude.sprocket.lsp

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import com.redhat.devtools.lsp4ij.client.features.LSPClientFeatures
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider
import org.stjude.sprocket.client.WdlFormattingFeature
import org.stjude.sprocket.client.WdlLanguageClient

class SprocketLanguageServerFactory : LanguageServerFactory {
    override fun createConnectionProvider(project: Project): StreamConnectionProvider = SprocketStreamConnectionProvider(project)

    override fun createLanguageClient(project: Project): LanguageClientImpl = WdlLanguageClient(project)

    override fun createClientFeatures(): LSPClientFeatures = LSPClientFeatures().setFormattingFeature(WdlFormattingFeature())
}
