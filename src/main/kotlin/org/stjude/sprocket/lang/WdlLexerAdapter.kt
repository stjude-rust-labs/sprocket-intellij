package org.stjude.sprocket.lang

import com.intellij.lexer.FlexAdapter

class WdlLexerAdapter : FlexAdapter(WdlLexer(null)) {
    override fun getState(): Int {
        return (flex as WdlLexer).packedState
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        (flex as WdlLexer).restorePackedState(initialState)
        super.start(
            buffer,
            startOffset,
            endOffset,
            initialState and 0x1F
        )
    }
}
