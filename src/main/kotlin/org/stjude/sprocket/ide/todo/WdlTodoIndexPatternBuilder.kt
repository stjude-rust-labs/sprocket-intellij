package org.stjude.sprocket.ide.todo

import com.intellij.lexer.Lexer
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.search.IndexPatternBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.stjude.sprocket.lang.WdlFile
import org.stjude.sprocket.lang.WdlLexerAdapter
import org.stjude.sprocket.lang.WdlTokenSets

class WdlTodoIndexPatternBuilder : IndexPatternBuilder {

    override fun getIndexingLexer(file: PsiFile): Lexer? = if (file is WdlFile) WdlLexerAdapter() else null
    override fun getCommentTokenSet(file: PsiFile): TokenSet? = if (file is WdlFile) WdlTokenSets.COMMENTS else null

    override fun getCommentStartDelta(tokenType: IElementType?): Int {
        return when (tokenType) {
            in WdlTokenSets.LINE_COMMENTS -> 1
            in WdlTokenSets.DOC_COMMENTS -> 2
            else -> 0
        }
    }

    override fun getCommentEndDelta(tokenType: IElementType?): Int = 0

    override fun getCharsAllowedInContinuationPrefix(tokenType: IElementType): String = ""
}