package org.stjude.sprocket.lang

import com.intellij.psi.tree.IElementType

class WdlTokenType(debugName: String) : IElementType(debugName, WdlLanguage) {
    override fun toString(): String {
        return "WdlTokenType." + super.toString()
    }
}
