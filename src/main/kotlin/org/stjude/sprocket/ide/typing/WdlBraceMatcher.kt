package org.stjude.sprocket.ide.typing

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.stjude.sprocket.lang.psi.WdlTokenTypes

class WdlBraceMatcher : PairedBraceMatcher {
    private val pairs = arrayOf(
        BracePair(WdlTokenTypes.L_BRACE, WdlTokenTypes.R_BRACE, true),
        BracePair(WdlTokenTypes.HEREDOC_OPEN, WdlTokenTypes.HEREDOC_CLOSE, true),
        BracePair(WdlTokenTypes.PLACEHOLDER_OPEN, WdlTokenTypes.PLACEHOLDER_CLOSE, false),
        BracePair(WdlTokenTypes.L_PAREN, WdlTokenTypes.R_PAREN, false),
        BracePair(WdlTokenTypes.L_SQUARE, WdlTokenTypes.R_SQUARE, false)
    )

    override fun getPairs(): Array<BracePair> = pairs

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}