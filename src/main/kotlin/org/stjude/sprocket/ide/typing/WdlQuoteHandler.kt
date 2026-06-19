package org.stjude.sprocket.ide.typing

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import org.stjude.sprocket.lang.psi.WdlTokenTypes

class WdlQuoteHandler : SimpleTokenSetQuoteHandler(
    WdlTokenTypes.QUOTE_DOUBLE,
    WdlTokenTypes.QUOTE_SINGLE
)
