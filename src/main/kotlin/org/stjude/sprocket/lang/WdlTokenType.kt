package org.stjude.sprocket.lang

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.stjude.sprocket.lang.psi.WdlTokenTypes

class WdlTokenType(debugName: String) : IElementType(debugName, WdlLanguage) {
    override fun toString(): String {
        return "WdlTokenType." + super.toString()
    }
}
