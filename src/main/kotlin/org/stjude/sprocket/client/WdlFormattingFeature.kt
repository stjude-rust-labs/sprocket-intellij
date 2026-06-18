package org.stjude.sprocket.client

import com.intellij.psi.PsiFile
import com.redhat.devtools.lsp4ij.client.features.LSPFormattingFeature


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
}