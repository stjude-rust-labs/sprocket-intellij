package org.stjude.sprocket.lang

import com.intellij.lexer.Lexer
import com.intellij.testFramework.LexerTestCase

class WdlLexerTest : LexerTestCase() {
    override fun createLexer(): Lexer {
        return WdlLexerAdapter()
    }

    override fun getDirPath(): String {
        return ""
    }

    fun testKeywords() {
        doTest("alias", "WdlTokenTypes.KW_ALIAS ('alias')")
        doTest("as", "WdlTokenTypes.KW_AS ('as')")
        doTest("call", "WdlTokenTypes.KW_CALL ('call')")
        doTest("command", "WdlTokenTypes.KW_COMMAND ('command')")
        doTest("else", "WdlTokenTypes.KW_ELSE ('else')")
        doTest("enum", "WdlTokenTypes.KW_ENUM ('enum')")
        doTest("hints", "WdlTokenTypes.KW_HINTS ('hints')")
        doTest("if", "WdlTokenTypes.KW_IF ('if')")
        doTest("in", "WdlTokenTypes.KW_IN ('in')")
        doTest("import", "WdlTokenTypes.KW_IMPORT ('import')")
        doTest("input", "WdlTokenTypes.KW_INPUT ('input')")
        doTest("left", "WdlTokenTypes.KW_LEFT ('left')")
        doTest("meta", "WdlTokenTypes.KW_META ('meta')")
        doTest("object", "WdlTokenTypes.KW_OBJECT ('object')")
        doTest("output", "WdlTokenTypes.KW_OUTPUT ('output')")
        doTest("parameter_meta", "WdlTokenTypes.KW_PARAMETER_META ('parameter_meta')")
        doTest("right", "WdlTokenTypes.KW_RIGHT ('right')")
        doTest("requirements", "WdlTokenTypes.KW_REQUIREMENTS ('requirements')")
        doTest("runtime", "WdlTokenTypes.KW_RUNTIME ('runtime')")
        doTest("scatter", "WdlTokenTypes.KW_SCATTER ('scatter')")
        doTest("struct", "WdlTokenTypes.KW_STRUCT ('struct')")
        doTest("task", "WdlTokenTypes.KW_TASK ('task')")
        doTest("then", "WdlTokenTypes.KW_THEN ('then')")
        doTest("version", "WdlTokenTypes.KW_VERSION ('version')")
        doTest("workflow", "WdlTokenTypes.KW_WORKFLOW ('workflow')")
        doTest("env", "WdlTokenTypes.KW_ENV ('env')")
    }
}