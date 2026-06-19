package org.stjude.sprocket.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parents
import org.stjude.sprocket.ide.highlight.WdlSyntaxHighlighter
import org.stjude.sprocket.lang.psi.WdlTokenTypes

/**
 * Contextual identifier highlighter.
 */
class WdlHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) return
        if (element !is LeafPsiElement) return
        val elementType = element.elementType

        if (elementType != WdlTokenTypes.IDENTIFIER) {
            return
        }

        val color = determineIdentColor(element) ?: return
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(color).create()
    }

    private fun determineIdentColor(element: PsiElement): TextAttributesKey? {
        val parent = element.parent ?: return null

        return when (parent.elementType) {
            WdlTokenTypes.DECLARATION,
            WdlTokenTypes.OUTPUT_DECLARATION,
            WdlTokenTypes.TASK_DECLARATION,
            WdlTokenTypes.HINTS_ENTRY-> WdlSyntaxHighlighter.DECLARATION
            WdlTokenTypes.META_ENTRY -> {
                // Need to iterate through all the parents since `META_OBJECT_VALUE` and `META_ARRAY_VALUE`
                // can appear within both `META_SECTION` and `PARAMETER_META_SECTION`
                for (metaParent in parent.parents(false)) {
                    return when (metaParent.elementType) {
                        WdlTokenTypes.META_SECTION,
                        WdlTokenTypes.META_OBJECT_VALUE,
                        WdlTokenTypes.META_ARRAY_VALUE -> WdlSyntaxHighlighter.META_ENTRY
                        WdlTokenTypes.PARAMETER_META_SECTION -> WdlSyntaxHighlighter.DECLARATION
                        else -> continue
                    }
                }

                null
            }
            WdlTokenTypes.WORKFLOW -> WdlSyntaxHighlighter.WORKFLOW
            WdlTokenTypes.TASK,
            WdlTokenTypes.CALL_STATEMENT,
            WdlTokenTypes.CALL_ALIAS,
            WdlTokenTypes.CALL_AFTER -> WdlSyntaxHighlighter.TASK
            WdlTokenTypes.STRUCT_ENTRY -> WdlSyntaxHighlighter.STRUCT_FIELD
            WdlTokenTypes.ENUM_CHOICE -> WdlSyntaxHighlighter.ENUM_CHOICE
            // TODO: Need to handle the case of `WdlTokenTypes.TYPE_NAME` for structs/enums, but we don't track those
            //       declarations yet.
            else -> null
        }
    }
}
