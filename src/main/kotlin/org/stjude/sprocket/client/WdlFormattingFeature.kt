package org.stjude.sprocket.client

import com.intellij.psi.PsiFile
import com.redhat.devtools.lsp4ij.client.features.LSPFormattingFeature
import org.stjude.sprocket.settings.SprocketSettings

class WdlFormattingFeature : LSPFormattingFeature() {
    /**
     * We have a custom PSI formatter for simple auto indentation of blocks.
     * For *full* formatting, we want to defer to the LSP.
     *
     * @see org.stjude.sprocket.ide.formatter.WdlFormattingModelBuilder
     */
    override fun isExistingFormatterOverrideable(file: PsiFile): Boolean {
        return true
    }

    /**
     * Gate LSP formatting on the "Enable formatting" setting. When disabled, the
     * PSI auto-indenter still applies; only the server's full formatting is suppressed.
     */
    override fun isEnabled(file: PsiFile): Boolean {
        return SprocketSettings.getInstance(file.project).format()
    }
}