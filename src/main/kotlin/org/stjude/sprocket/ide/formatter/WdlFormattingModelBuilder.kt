package org.stjude.sprocket.ide.formatter

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.formatting.SpacingBuilder
import org.stjude.sprocket.lang.WdlLanguage
import org.stjude.sprocket.lang.WdlTokenSets
import org.stjude.sprocket.lang.psi.WdlTokenTypes

/**
 * WDL formatting model builder.
 *
 * All this extra machinery is needed to support auto-indentation
 * when creating blocks.
 *
 * For example, typing:
 *
 * ```
 * task foo {<cursor>}
 * ```
 *
 * Then inserting a newline between the braces will result in:
 *
 * ```
 * task foo {
 * <indent><cursor>
 * }
 * ```
 *
 * @see org.stjude.sprocket.ide.typing.WdlBraceMatcher
 * @see org.stjude.sprocket.ide.formatter.WdlBlock
 */
class WdlFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings

        val spacingBuilder = SpacingBuilder(settings, WdlLanguage)
            .after(WdlTokenTypes.COMMA).spacing(1, 1, 0, true, 0)
            .before(WdlTokenTypes.COMMA).spaceIf(false)
            // Always put spaces after colons
            .after(WdlTokenTypes.COLON).spaceIf(true)
            .before(WdlTokenTypes.COLON).spaceIf(false)
            // Remove spaces between empty parens/brackets
            //
            // For example:
            //
            // task foo {
            // }
            //
            // Goes to:
            //
            // task foo {}
            .between(WdlTokenTypes.L_PAREN, WdlTokenTypes.R_PAREN).spacing(0, 0, 0, true, 0)
            .between(WdlTokenTypes.L_SQUARE, WdlTokenTypes.R_SQUARE).spacing(0, 0, 0, true, 0)
            .between(WdlTokenTypes.L_BRACE, WdlTokenTypes.R_BRACE).spacing(0, 0, 0, true, 0)
            // Super simple black handling to keep things looking *okay* at least.
            //
            // `sprocket format` can handle all the complexities of keeping the WDL actually formatted.
            .afterInside(WdlTokenTypes.L_BRACE, WdlTokenSets.BLOCKS).parentDependentLFSpacing(1, 1, true, 0)
            .beforeInside(WdlTokenTypes.R_BRACE, WdlTokenSets.BLOCKS).parentDependentLFSpacing(1, 1, true, 0)

        val block = WdlBlock(
            formattingContext.node,
            null,
            null,
            Indent.getNoneIndent(),
            spacingBuilder
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            block,
            settings
        )
    }
}
