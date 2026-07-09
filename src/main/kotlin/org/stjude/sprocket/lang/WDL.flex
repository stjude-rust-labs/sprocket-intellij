package org.stjude.sprocket.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import static org.stjude.sprocket.lang.psi.WdlTokenTypes.*;

%%
%public
%class WdlLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%{
    private int stateStack = 0;
    private int placeholderBraceDepth = 0;

    public void yypushState(int newState) {
        stateStack = (stateStack << 5) | yystate();
        yybegin(newState);
    }

    public void yypopState() {
        int prevState = stateStack & 0x1F;
        stateStack >>>= 5;
        yybegin(prevState);
    }

    /**
    * Determines the appropriate token type when encountering a closing brace
    * in a placeholder.
    */
    public IElementType placeholderCloseBrace() {
        if (placeholderBraceDepth == 0) {
            yypopState();
            return PLACEHOLDER_CLOSE;
        }

        placeholderBraceDepth -= 1;
        return R_BRACE;
    }

    public int getPackedState() {
        return (stateStack << 5) | yystate();
    }

    public void restorePackedState(int packedState) {
        int currentState = packedState & 0x1F;
        stateStack = packedState >>> 5;
        yybegin(currentState);
    }
%}

// Braces
L_BRACE="{"
R_BRACE="}"
L_PAREN="("
R_PAREN=")"
L_SQUARE="["
R_SQUARE="]"
HEREDOC_OPEN="<<<"
HEREDOC_CLOSE=">>>"

// Keywords
KW_ALIAS="alias"
KW_AS="as"
KW_AFTER="after"
KW_CALL="call"
KW_COMMAND="command"
KW_ELSE="else"
KW_ENUM="enum"
KW_HINTS="hints"
KW_IF="if"
KW_IN="in"
KW_IMPORT="import"
KW_INPUT="input"
KW_LEFT="left"
KW_META="meta"
KW_OBJECT="object"
KW_OUTPUT="output"
KW_PARAMETER_META="parameter_meta"
KW_RIGHT="right"
KW_REQUIREMENTS="requirements"
KW_RUNTIME="runtime"
KW_SCATTER="scatter"
KW_STRUCT="struct"
KW_TASK="task"
KW_THEN="then"
KW_VERSION="version"
KW_WORKFLOW="workflow"
KW_ENV="env"
KW_FROM="from"

// Types
ARRAY_TYPE="Array"
BOOLEAN_TYPE="Boolean"
DIRECTORY_TYPE="Directory"
FILE_TYPE="File"
FLOAT_TYPE="Float"
INT_TYPE="Int"
MAP_TYPE="Map"
OBJECT_TYPE="Object"
PAIR_TYPE="Pair"
STRING_TYPE="String"

NONE="None"
NULL="null"
BOOLEAN="true"|"false"
DIGIT=[:digit:]
DECIMAL_INTEGER={DIGIT}+
OCTAL_INTEGER=0[oO][0-7]+|0[0-7]+
HEX_INTEGER=0[xX][{DIGIT}|[a-fA-F]]+
NUMBER={DECIMAL_INTEGER}|{HEX_INTEGER}|{OCTAL_INTEGER}
EXPONENT=[eE][+-]?[0-9]+
FLOAT = ({DECIMAL_INTEGER}"."{DIGIT}*|"."{DECIMAL_INTEGER})({EXPONENT})?
      | {DECIMAL_INTEGER}{EXPONENT}

// Placeholders
PLACEHOLDER_OPEN_TILDE=\~\{
PLACEHOLDER_OPEN_DOLLAR=\$\{

// Placeholder options (deprecated)
PLACEHOLDER_SEP=("sep"[\ |\t]*{EQUAL})
PLACEHOLDER_DEFAULT=("default"[\ |\t]*{EQUAL})
PLACEHOLDER_TRUE=("true"[\ |\t]*{EQUAL})
PLACEHOLDER_FALSE=("false"[\ |\t]*{EQUAL})

// Strings
QUOTE_SINGLE=\'
QUOTE_DOUBLE=\"
ESCAPE_SEQUENCE=\\.

COLON=":"
DOUBLE_EQUAL="=="
NOT_EQUAL="!="
EQUAL="="
DOT="."
QMARK="?"
PLUS="+"
DASH="-"
ASTERISK=\*
DOUBLE_ASTERISK=\*\*
SLASH=\/
PERCENT=\%
LOGICAL_NOT="!"
COMMA=","
DOUBLE_PIPE=\|\|
DOUBLE_AMPERSAND="&&"
LESS_THAN=\<
LESS_EQUAL=\<\=
MORE_THAN=\>
MORE_EQUAL=\>\=

COMMENT=("#")[^\r\n]*
DOC_COMMENT=("##")[^\r\n]*

WHITE_SPACE= \n|\r|\r\n|\ |\t|\f
IDENTIFIER=[:letter:]([:letter:]|[:digit:]|\_)*

%state IN_COMMAND
%state BRACE_COMMAND
%state HEREDOC_COMMAND
%state PLACEHOLDER
%state D_QUOTE
%state S_QUOTE
%state MULTILINE_STRING

%%

// Whitespace & comments
<YYINITIAL, PLACEHOLDER, IN_COMMAND> {
    {WHITE_SPACE} { return TokenType.WHITE_SPACE; }
    {DOC_COMMENT} { return DOC_COMMENT; }
    {COMMENT}     { return COMMENT; }
}

// String literals
<D_QUOTE, S_QUOTE, MULTILINE_STRING> {ESCAPE_SEQUENCE}    { return ESCAPE_SEQUENCE; }
<D_QUOTE> [^[\"\n]]                         { return STRING_CONTENT; }
<S_QUOTE> [^[\'\n]]                         { return STRING_CONTENT; }
<MULTILINE_STRING> [^<]                 { return STRING_CONTENT; }
<YYINITIAL, PLACEHOLDER> {QUOTE_DOUBLE} { yypushState(D_QUOTE); return QUOTE_DOUBLE; }
<D_QUOTE>   {QUOTE_DOUBLE}              { yypopState(); return QUOTE_DOUBLE; }
<YYINITIAL, PLACEHOLDER> {QUOTE_SINGLE} { yypushState(S_QUOTE); return QUOTE_SINGLE; }
<S_QUOTE>   {QUOTE_SINGLE}              { yypopState(); return QUOTE_SINGLE; }
<YYINITIAL, PLACEHOLDER> {HEREDOC_OPEN} { yypushState(MULTILINE_STRING); return HEREDOC_OPEN; }
<MULTILINE_STRING>   {HEREDOC_CLOSE}     { yypopState(); return HEREDOC_CLOSE; }

// Placeholders
<MULTILINE_STRING, D_QUOTE, S_QUOTE, BRACE_COMMAND, HEREDOC_COMMAND> {PLACEHOLDER_OPEN_TILDE} { yypushState(PLACEHOLDER); return PLACEHOLDER_OPEN; }
<MULTILINE_STRING, D_QUOTE, S_QUOTE, BRACE_COMMAND> {PLACEHOLDER_OPEN_DOLLAR}                 { yypushState(PLACEHOLDER); return PLACEHOLDER_OPEN; }
<PLACEHOLDER> {
    {L_BRACE}                    { placeholderBraceDepth++; return L_BRACE; }
    {R_BRACE}                    { return placeholderCloseBrace(); }
    {PLACEHOLDER_DEFAULT}        { return PLACEHOLDER_OPTION_DEFAULT; }
    {PLACEHOLDER_SEP}            { return PLACEHOLDER_OPTION_SEP; }
    {PLACEHOLDER_TRUE}           { return PLACEHOLDER_OPTION_TRUE; }
    {PLACEHOLDER_FALSE}          { return PLACEHOLDER_OPTION_FALSE; }
}

// Command section
<YYINITIAL> {KW_COMMAND}           { yybegin(IN_COMMAND); return KW_COMMAND; }
<IN_COMMAND> {
    {L_BRACE}                      { yybegin(BRACE_COMMAND); return L_BRACE; }
    {HEREDOC_OPEN}                 { yybegin(HEREDOC_COMMAND); return HEREDOC_OPEN; }
    [^]                            { yypushback(1); yybegin(YYINITIAL); }
}
<BRACE_COMMAND> {
    {R_BRACE}          { yybegin(YYINITIAL); return R_BRACE; }
    [^~$\}]+           { return COMMAND_TEXT; }
    "~"                { return COMMAND_TEXT; }
    "$"                { return COMMAND_TEXT; }
}
<HEREDOC_COMMAND> {
    {HEREDOC_CLOSE}    { yybegin(YYINITIAL); return HEREDOC_CLOSE; }
    [^~>]+             { return COMMAND_TEXT; }
    "~"                { return COMMAND_TEXT; }
    ">"                { return COMMAND_TEXT; }
}

<YYINITIAL, PLACEHOLDER> {
    // Keywords
    {KW_VERSION}           { return KW_VERSION; }
    {KW_WORKFLOW}          { return KW_WORKFLOW; }
    {KW_TASK}              { return KW_TASK; }
    {KW_STRUCT}            { return KW_STRUCT; }
    {KW_ENUM}              { return KW_ENUM; }
    {KW_META}              { return KW_META; }
    {KW_PARAMETER_META}    { return KW_PARAMETER_META; }
    {KW_RUNTIME}           { return KW_RUNTIME; }
    {KW_REQUIREMENTS}      { return KW_REQUIREMENTS; }
    {KW_HINTS}             { return KW_HINTS; }
    {KW_CALL}              { return KW_CALL; }
    {KW_AS}                { return KW_AS; }
    {KW_AFTER}             { return KW_AFTER; }
    {KW_INPUT}             { return KW_INPUT; }
    {KW_OUTPUT}            { return KW_OUTPUT; }
    {KW_SCATTER}           { return KW_SCATTER; }
    {KW_IN}                { return KW_IN; }
    {KW_LEFT}              { return KW_LEFT; }
    {KW_RIGHT}             { return KW_RIGHT; }
    {KW_OBJECT}            { return KW_OBJECT; }
    {KW_IMPORT}            { return KW_IMPORT; }
    {KW_ALIAS}             { return KW_ALIAS; }
    {KW_ENV}               { return KW_ENV; }
    {KW_IF}                { return KW_IF; }
    {KW_THEN}              { return KW_THEN; }
    {KW_ELSE}              { return KW_ELSE; }
    {KW_FROM}              { return KW_FROM; }

    // Primitives
    ({BOOLEAN_TYPE}
    |{INT_TYPE}
    |{FLOAT_TYPE}
    |{STRING_TYPE}
    |{FILE_TYPE}
    |{DIRECTORY_TYPE})     { return PRIMITIVE_TYPE; }

    // Types
    {ARRAY_TYPE}           { return ARRAY_TYPE; }
    {MAP_TYPE}             { return MAP_TYPE; }
    {OBJECT_TYPE}          { return OBJECT_TYPE; }
    {PAIR_TYPE}            { return PAIR_TYPE; }
    {NONE}                 { return NONE; }
    {NULL}                 { return NULL; }

    // Literals
    {BOOLEAN}              { return BOOLEAN; }
    {NUMBER}               { return NUMBER; }
    {FLOAT}                { return FLOAT; }

    // Punctuation
    {L_BRACE}              { return L_BRACE; }
    {R_BRACE}              { return R_BRACE; }
    {L_PAREN}              { return L_PAREN; }
    {R_PAREN}              { return R_PAREN; }
    {L_SQUARE}             { return L_SQUARE; }
    {R_SQUARE}             { return R_SQUARE; }
    {COLON}                { return COLON; }
    {DOUBLE_EQUAL}         { return DOUBLE_EQUAL; }
    {NOT_EQUAL}            { return NOT_EQUAL; }
    {EQUAL}                { return EQUAL; }
    {DOT}                  { return DOT; }
    {QMARK}                { return QMARK; }
    {PLUS}                 { return PLUS; }
    {DASH}                 { return DASH; }
    {DOUBLE_ASTERISK}      { return DOUBLE_ASTERISK; }
    {ASTERISK}             { return ASTERISK; }
    {SLASH}                { return SLASH; }
    {PERCENT}              { return PERCENT; }
    {LOGICAL_NOT}          { return LOGICAL_NOT; }
    {COMMA}                { return COMMA; }
    {DOUBLE_PIPE}          { return DOUBLE_PIPE; }
    {DOUBLE_AMPERSAND}     { return DOUBLE_AMPERSAND; }
    {LESS_EQUAL}           { return LESS_EQUAL; }
    {LESS_THAN}            { return LESS_THAN; }
    {MORE_EQUAL}           { return MORE_EQUAL; }
    {MORE_THAN}            { return MORE_THAN; }

    {IDENTIFIER}           { return IDENTIFIER; }
}

// Catch-all
[^] { return TokenType.BAD_CHARACTER; }
