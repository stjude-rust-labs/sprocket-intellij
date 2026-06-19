package org.stjude.sprocket.lang

import com.intellij.psi.tree.IElementType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.stjude.sprocket.lang.psi.WdlTokenTypes

class WdlLexerTest {

    @Test
    fun `keywords lex to a single keyword token`() {
        assertSingleToken("alias", WdlTokenTypes.KW_ALIAS)
        assertSingleToken("as", WdlTokenTypes.KW_AS)
        assertSingleToken("call", WdlTokenTypes.KW_CALL)
        assertSingleToken("command", WdlTokenTypes.KW_COMMAND)
        assertSingleToken("else", WdlTokenTypes.KW_ELSE)
        assertSingleToken("enum", WdlTokenTypes.KW_ENUM)
        assertSingleToken("hints", WdlTokenTypes.KW_HINTS)
        assertSingleToken("if", WdlTokenTypes.KW_IF)
        assertSingleToken("in", WdlTokenTypes.KW_IN)
        assertSingleToken("import", WdlTokenTypes.KW_IMPORT)
        assertSingleToken("input", WdlTokenTypes.KW_INPUT)
        assertSingleToken("left", WdlTokenTypes.KW_LEFT)
        assertSingleToken("meta", WdlTokenTypes.KW_META)
        assertSingleToken("object", WdlTokenTypes.KW_OBJECT)
        assertSingleToken("output", WdlTokenTypes.KW_OUTPUT)
        assertSingleToken("parameter_meta", WdlTokenTypes.KW_PARAMETER_META)
        assertSingleToken("right", WdlTokenTypes.KW_RIGHT)
        assertSingleToken("requirements", WdlTokenTypes.KW_REQUIREMENTS)
        assertSingleToken("runtime", WdlTokenTypes.KW_RUNTIME)
        assertSingleToken("scatter", WdlTokenTypes.KW_SCATTER)
        assertSingleToken("struct", WdlTokenTypes.KW_STRUCT)
        assertSingleToken("task", WdlTokenTypes.KW_TASK)
        assertSingleToken("then", WdlTokenTypes.KW_THEN)
        assertSingleToken("version", WdlTokenTypes.KW_VERSION)
        assertSingleToken("workflow", WdlTokenTypes.KW_WORKFLOW)
        assertSingleToken("env", WdlTokenTypes.KW_ENV)
    }

    private fun assertSingleToken(text: String, expected: IElementType) {
        val lexer = WdlLexerAdapter()
        lexer.start(text, 0, text.length, 0)

        assertEquals(expected, lexer.tokenType, "token type for `$text`")
        assertEquals(text, lexer.tokenText, "token text for `$text`")

        lexer.advance()
        assertNull(lexer.tokenType, "`$text` should lex to a single token")
    }
}
