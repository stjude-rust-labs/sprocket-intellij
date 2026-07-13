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
    override fun getLanguagesToInject(
        registrar: MultiHostRegistrar,
        host: PsiElement,
    ) {
        if (host !is WdlCommandBlock) return

        val children =
            PsiTreeUtil
                .findChildrenOfAnyType(host, WdlCommandContentText::class.java, WdlPlaceholder::class.java)
                .toList()
        val lastContentIndex = children.indexOfLast { it is WdlCommandContentText }
        if (lastContentIndex < 0) return

        registrar.startInjecting(ShLanguage.INSTANCE)

        // `sh` can't see the WDL placeholders (`~{...}`/`${...}`), so stand each one in with a
        // `dummy` token to keep the surrounding shell syntax intact. Placeholders between content
        // fragments become a prefix on the next fragment; any trailing the last fragment become a
        // suffix on it.
        var pendingPlaceholders = 0
        children.forEachIndexed { index, child ->
            when (child) {
                is WdlPlaceholder -> pendingPlaceholders++
                is WdlCommandContentText -> {
                    val prefix = if (pendingPlaceholders > 0) "dummy".repeat(pendingPlaceholders) else null
                    pendingPlaceholders = 0

                    val suffix =
                        if (index == lastContentIndex) {
                            val trailing = children.drop(index + 1).count { it is WdlPlaceholder }
                            if (trailing > 0) "dummy".repeat(trailing) else null
                        } else {
                            null
                        }

                    registrar.addPlace(prefix, suffix, child, TextRange(0, child.textLength))
                }
            }
        }

        registrar.doneInjecting()
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> = listOf(WdlCommandBlock::class.java)
}
