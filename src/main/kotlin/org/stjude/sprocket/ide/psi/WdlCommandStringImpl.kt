package org.stjude.sprocket.ide.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost

/**
 * Wrapper for command text tokens to make them [PsiLanguageInjectionHost]s.
 *
 * @see org.stjude.sprocket.ide.injection.WdlCommandHighlightFilter
 */
abstract class WdlCommandStringImpl(node: ASTNode) : ASTWrapperPsiElement(node), PsiLanguageInjectionHost {

    override fun isValidHost(): Boolean = true

    /**
     * Intentionally a no-op. Command text is a read-only injection host, so edits made inside
     * the injected `sh` fragment (e.g. a rename from a shell intention) are not written back to
     * the WDL document. Propagating them would require rebuilding the command-content PSI from
     * the edited text, which is not worth the complexity for these highlighting-only injections.
     */
    override fun updateText(text: String): PsiLanguageInjectionHost {
        return this
    }

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return LiteralTextEscaper.createSimple(this)
    }
}
