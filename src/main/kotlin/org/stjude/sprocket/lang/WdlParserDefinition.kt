package org.stjude.sprocket.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.stjude.sprocket.lang.parser.WdlParser
import org.stjude.sprocket.lang.psi.WdlTokenTypes

class WdlParserDefinition : ParserDefinition {
    private val FILE: IFileElementType = IFileElementType(WdlLanguage)
    override fun createLexer(project: Project?): Lexer {
        return WdlLexerAdapter()
    }

    override fun createParser(project: Project?): PsiParser {
        return WdlParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun getCommentTokens(): TokenSet {
        return WdlTokenSets.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.create(WdlTokenTypes.STRING_LITERAL)
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return WdlTokenTypes.Factory.createElement(node)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return WdlFile(viewProvider)
    }

}
