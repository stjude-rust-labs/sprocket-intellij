package org.stjude.sprocket.ide.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.stjude.sprocket.lang.WdlLexerAdapter
import org.stjude.sprocket.lang.WdlTokenSets

class WdlSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = WdlLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        when {
            WdlTokenSets.PRIMITIVE_TYPES.contains(tokenType) -> arrayOf(KEYWORD)
            WdlTokenSets.KEYWORDS.contains(tokenType) -> arrayOf(KEYWORD)
            WdlTokenSets.OPERATORS.contains(tokenType) -> arrayOf(OPERATOR)
            WdlTokenSets.NUMBERS.contains(tokenType) -> arrayOf(NUMBERS)
            WdlTokenSets.STRINGS.contains(tokenType) -> arrayOf(STRING)
            WdlTokenSets.LINE_COMMENTS.contains(tokenType) -> arrayOf(COMMENT)
            WdlTokenSets.DOC_COMMENTS.contains(tokenType) -> arrayOf(DOC_COMMENT)
            WdlTokenSets.BRACES.contains(tokenType) -> arrayOf(BRACES)
            WdlTokenSets.PARENS.contains(tokenType) -> arrayOf(PARENS)
            WdlTokenSets.BRACKETS.contains(tokenType) -> arrayOf(BRACKETS)
            else -> emptyArray<TextAttributesKey>()
        }

    companion object {
        val KEYWORD = TextAttributesKey.createTextAttributesKey("WDL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val STRING = TextAttributesKey.createTextAttributesKey("WDL_STRING", DefaultLanguageHighlighterColors.STRING)
        val COMMENT = TextAttributesKey.createTextAttributesKey("WDL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val DOC_COMMENT = TextAttributesKey.createTextAttributesKey("WDL_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT)
        val OPERATOR = TextAttributesKey.createTextAttributesKey("WDL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val NUMBERS = TextAttributesKey.createTextAttributesKey("WDL_NUMBERS", DefaultLanguageHighlighterColors.NUMBER)
        val PARENS = TextAttributesKey.createTextAttributesKey("WDL_PARENS", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES = TextAttributesKey.createTextAttributesKey("WDL_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = TextAttributesKey.createTextAttributesKey("WDL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val TASK = TextAttributesKey.createTextAttributesKey("WDL_TASK", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val WORKFLOW = TextAttributesKey.createTextAttributesKey("WDL_WORKFLOW", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val META_ENTRY = TextAttributesKey.createTextAttributesKey("WDL_META_ENTRY", DefaultLanguageHighlighterColors.METADATA)
        val DECLARATION = TextAttributesKey.createTextAttributesKey("WDL_DECLARATION", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)
        val STRUCT = TextAttributesKey.createTextAttributesKey("WDL_STRUCT", DefaultLanguageHighlighterColors.CLASS_NAME)
        val STRUCT_FIELD = TextAttributesKey.createTextAttributesKey("WDL_STRUCT_FIELD", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val ENUM = TextAttributesKey.createTextAttributesKey("WDL_ENUM", DefaultLanguageHighlighterColors.CLASS_NAME)
        val ENUM_CHOICE = TextAttributesKey.createTextAttributesKey("WDL_ENUM_CHOICE", DefaultLanguageHighlighterColors.STATIC_FIELD)
    }
}
