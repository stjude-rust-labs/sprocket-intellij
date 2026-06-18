package org.stjude.sprocket.ide.injection

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.sh.ShLanguage
import org.stjude.sprocket.lang.psi.WdlCommandBlock
import org.stjude.sprocket.lang.psi.WdlCommandContentText
import org.stjude.sprocket.lang.psi.WdlPlaceholder

/**
 * Injects the `sh` language into `command` sections.
 */
class WdlCommandLanguageInjector : MultiHostInjector {

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, host: PsiElement) {
        if (host !is WdlCommandBlock) return

        var inPlaceholder = false
        var hasStartedInjection = false

        val elements = PsiTreeUtil.findChildrenOfAnyType(host, WdlCommandContentText::class.java, WdlPlaceholder::class.java)
        for (child in elements) {
            when (child) {
                is WdlCommandContentText -> {
                    if (!hasStartedInjection) {
                        registrar.startInjecting(ShLanguage.INSTANCE)
                        hasStartedInjection = true
                    }

                    registrar.addPlace(
                        // Have to add some dummy text in place of placeholders so that the `sh`
                        // plugin doesn't get tripped up on the gaps
                        if (inPlaceholder) "dummy" else null,
                        null,
                        child,
                        TextRange(0, child.textLength)
                    )

                    inPlaceholder = false
                }
                is WdlPlaceholder -> inPlaceholder = true
            }
        }

        if (hasStartedInjection) {
            registrar.doneInjecting()
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> {
        return listOf(WdlCommandBlock::class.java)
    }
}