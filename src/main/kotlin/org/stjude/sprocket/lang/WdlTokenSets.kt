package org.stjude.sprocket.lang

import com.intellij.psi.tree.TokenSet
import org.stjude.sprocket.lang.psi.WdlTokenTypes

object WdlTokenSets {
    val PRIMITIVE_TYPES = TokenSet.create(WdlTokenTypes.PRIMITIVE_TYPE)
    val KEYWORDS = TokenSet.orSet(
        PRIMITIVE_TYPES,
        TokenSet.create(
            WdlTokenTypes.KW_VERSION,
            WdlTokenTypes.KW_IMPORT,
            WdlTokenTypes.KW_ALIAS,
            WdlTokenTypes.KW_STRUCT,
            WdlTokenTypes.KW_ENUM,
            WdlTokenTypes.KW_TASK,
            WdlTokenTypes.KW_WORKFLOW,
            WdlTokenTypes.KW_INPUT,
            WdlTokenTypes.KW_ENV,
            WdlTokenTypes.KW_OUTPUT,
            WdlTokenTypes.KW_META,
            WdlTokenTypes.KW_PARAMETER_META,
            WdlTokenTypes.KW_SCATTER,
            WdlTokenTypes.KW_CALL,
            WdlTokenTypes.KW_IF,
            WdlTokenTypes.KW_ELSE,
            WdlTokenTypes.KW_THEN,
            WdlTokenTypes.KW_HINTS,
            WdlTokenTypes.KW_RUNTIME,
            WdlTokenTypes.KW_REQUIREMENTS,
            WdlTokenTypes.KW_AS,
            WdlTokenTypes.KW_LEFT,
            WdlTokenTypes.KW_RIGHT,
            WdlTokenTypes.KW_IN,
            WdlTokenTypes.KW_COMMAND,
            WdlTokenTypes.KW_OBJECT
        ),
    )
    val OPERATORS = TokenSet.create(
        WdlTokenTypes.COLON,
        WdlTokenTypes.DOUBLE_EQUAL,
        WdlTokenTypes.NOT_EQUAL,
        WdlTokenTypes.EQUAL,
        WdlTokenTypes.DOT,
        WdlTokenTypes.QMARK,
        WdlTokenTypes.PLUS,
        WdlTokenTypes.DASH,
        WdlTokenTypes.ASTERISK,
        WdlTokenTypes.SLASH,
        WdlTokenTypes.PERCENT,
        WdlTokenTypes.LOGICAL_NOT,
        WdlTokenTypes.COMMA,
        WdlTokenTypes.DOUBLE_PIPE,
        WdlTokenTypes.DOUBLE_AMPERSAND,
        WdlTokenTypes.LESS_THAN,
        WdlTokenTypes.LESS_EQUAL,
        WdlTokenTypes.MORE_THAN,
        WdlTokenTypes.MORE_EQUAL,
    )
    val OPEN_BRACES = TokenSet.create(WdlTokenTypes.L_BRACE, WdlTokenTypes.PLACEHOLDER_OPEN, WdlTokenTypes.HEREDOC_OPEN)
    val CLOSE_BRACES = TokenSet.create(WdlTokenTypes.R_BRACE, WdlTokenTypes.PLACEHOLDER_CLOSE, WdlTokenTypes.HEREDOC_CLOSE)

    val BRACES = TokenSet.orSet(
        OPEN_BRACES,
        CLOSE_BRACES
    )

    val OPEN_BRACKETS = TokenSet.create(WdlTokenTypes.L_SQUARE)
    val CLOSE_BRACKETS = TokenSet.create(WdlTokenTypes.R_SQUARE)

    val BRACKETS = TokenSet.orSet(OPEN_BRACKETS, CLOSE_BRACKETS)

    val PARENS = TokenSet.create(WdlTokenTypes.L_PAREN, WdlTokenTypes.R_PAREN)
    val STRINGS = TokenSet.create(
        WdlTokenTypes.QUOTE_DOUBLE,
        WdlTokenTypes.QUOTE_SINGLE,
        WdlTokenTypes.STRING_CONTENT,
    )
    val LINE_COMMENTS = TokenSet.create(WdlTokenTypes.COMMENT)
    val DOC_COMMENTS = TokenSet.create(WdlTokenTypes.DOC_COMMENT)
    val COMMENTS = TokenSet.orSet(LINE_COMMENTS, DOC_COMMENTS)
    val NUMBERS = TokenSet.create(WdlTokenTypes.NUMBER, WdlTokenTypes.FLOAT)

    val COMMAND_BLOCKS = TokenSet.create(
        WdlTokenTypes.CURLY_COMMAND_BLOCK,
        WdlTokenTypes.HEREDOC_COMMAND_BLOCK,
    )
    val BLOCKS = TokenSet.orSet(
        COMMAND_BLOCKS,
        TokenSet.create(
            WdlTokenTypes.WORKFLOW,
            WdlTokenTypes.TASK,
            WdlTokenTypes.STRUCT,
            WdlTokenTypes.ENUM,
            WdlTokenTypes.CALL_INPUTS_BLOCK,
        )
    )
    val BLOCK_OPEN_DELIMITERS =
        TokenSet.orSet(OPEN_BRACES, OPEN_BRACKETS)
    val BLOCK_DELIMITERS = TokenSet.orSet(BRACES, BRACKETS)
}
