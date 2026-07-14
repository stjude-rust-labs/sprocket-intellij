package org.stjude.sprocket.ide.typing

import com.intellij.application.options.CodeStyle
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate.Result
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.util.text.CharArrayUtil
import org.stjude.sprocket.lang.WdlFile
import org.stjude.sprocket.lang.psi.WdlTokenTypes

/**
 * Handles "smart" auto-indentation of heredoc `command` sections.
 *
 * For example, pressing enter here:
 *
 * `command <<<{cursor}>>>`
 *
 * Will result in:
 *
 * ```
 * command <<<
 * {indent}{cursor}
 * >>>
 * ```
 */
class WdlEnterInCommandHandler : EnterHandlerDelegateAdapter() {
    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffsetRef: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?,
    ): Result {
        if (file !is WdlFile) return Result.Continue

        // Get current document and commit any changes, so we'll get the latest PSI
        val document = editor.document
        PsiDocumentManager.getInstance(file.project).commitDocument(document)

        val caretOffset = caretOffsetRef.get()
        if (caretOffset == 0) return Result.Continue

        val text = document.charsSequence

        // Find tokens surrounding the caret
        val rightOffset = CharArrayUtil.shiftForward(text, caretOffset, " \t")
        val leftOffset = CharArrayUtil.shiftBackward(text, caretOffset - 1, " \t")
        if (leftOffset < 0 || rightOffset >= text.length) return Result.Continue

        val leftElement = file.findElementAt(leftOffset) ?: return Result.Continue
        val rightElement = file.findElementAt(rightOffset) ?: return Result.Continue

        val parentType = leftElement.parent.node.elementType
        if (parentType != WdlTokenTypes.HEREDOC_COMMAND_BLOCK ||
            leftElement.node.elementType != WdlTokenTypes.HEREDOC_OPEN ||
            rightElement.node.elementType != WdlTokenTypes.HEREDOC_CLOSE
        ) {
            // Not in a heredoc command block
            return Result.Continue
        }

        // Calculate indentation for the opening bracket's line
        val lineNumber = document.getLineNumber(leftOffset)
        val lineStartOffset = document.getLineStartOffset(lineNumber)

        // Find the actual indentation (leading whitespace only)
        var indentEnd = lineStartOffset
        while (indentEnd < text.length && (text[indentEnd] == ' ' || text[indentEnd] == '\t')) {
            indentEnd++
        }
        val baseIndent = text.substring(lineStartOffset, indentEnd)

        // Get the indent size from code style settings
        val codeStyleSettings = CodeStyle.getSettings(file)
        val indentString =
            if (codeStyleSettings.useTabCharacter(file.fileType)) {
                "\t"
            } else {
                " ".repeat(codeStyleSettings.getIndentSize(file.fileType))
            }

        val startReplaceOffset = leftElement.textRange.endOffset
        val endReplaceOffset = rightElement.textRange.startOffset

        val textToInsert = "\n$baseIndent$indentString\n$baseIndent"
        document.replaceString(startReplaceOffset, endReplaceOffset, textToInsert)

        // Position cursor after the first newline and the newly combined indents
        val newCaretOffset = startReplaceOffset + 1 + baseIndent.length + indentString.length
        editor.caretModel.moveToOffset(newCaretOffset)

        return Result.Stop
    }
}
