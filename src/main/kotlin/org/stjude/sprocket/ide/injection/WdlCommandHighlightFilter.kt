package org.stjude.sprocket.ide.injection

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoFilter
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfType
import org.stjude.sprocket.lang.WdlFile
import org.stjude.sprocket.lang.psi.WdlCommandContentText

/**
 * Overrides the highlighting to completely skip `command` sections. We defer to the `sh` plugin for that.
 *
 * @see org.stjude.sprocket.ide.injection.WdlCommandLanguageInjector
 */
class WdlCommandHighlightFilter : HighlightInfoFilter {
    override fun accept(highlightInfo: HighlightInfo, file: PsiFile?): Boolean {
        if (file == null || file !is WdlFile) return true

        if (highlightInfo.severity != HighlightSeverity.INFORMATION) {
            return true
        }

        val startElement = file.findElementAt(highlightInfo.startOffset) ?: return true
        val endElement = file.findElementAt(maxOf(0, highlightInfo.endOffset - 1)) ?: return true

        val isCommandContent =
            (startElement.parentOfType<WdlCommandContentText>(withSelf = true) != null && endElement.parentOfType<WdlCommandContentText>(
                withSelf = true
            ) != null)
        return !isCommandContent
    }
}
