package org.stjude.sprocket.ide.formatter

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import org.stjude.sprocket.lang.WdlTokenSets

/**
 * @see org.stjude.sprocket.ide.formatter.WdlFormattingModelBuilder
 */
class WdlBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val indent: Indent?,
    private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {
    override fun buildChildren(): List<Block> {
        val children = myNode.getChildren(null)
            .filter { it.textLength > 0 && it.elementType !== TokenType.WHITE_SPACE }
            .map { childNode: ASTNode ->
                val childIndent = computeChildIndent(childNode)
                WdlBlock(childNode, null, null, childIndent, spacingBuilder)
            }

        return children
    }

    private fun isBlockNode(node: ASTNode): Boolean {
        if (WdlTokenSets.BLOCKS.contains(node.elementType)) return true
        return node.findChildByType(WdlTokenSets.BLOCK_OPEN_DELIMITERS) != null
    }

    private fun computeChildIndent(child: ASTNode): Indent {
        val childType = child.elementType

        val isParentWdlBlock = isBlockNode(myNode)
        val isChildBraceType = WdlTokenSets.BLOCK_DELIMITERS.contains(childType)

        // * If the parent of the node isn't a block, it shouldn't be indented at all
        // * If a brace is a child of a block, keep it flush with the parent
        if (!isParentWdlBlock || isChildBraceType) {
            return Indent.getNoneIndent()
        }

        // We're inside of a block, let's find its opening brace
        val openingBrace = myNode.findChildByType(WdlTokenSets.BLOCK_OPEN_DELIMITERS)

        // Only indent the elements that occur AFTER the opening brace
        if (openingBrace == null || child.startOffset <= openingBrace.startOffset) {
            return Indent.getNoneIndent()
        }

        return Indent.getNormalIndent()
    }

    override fun getIndent(): Indent? = indent

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        if (isBlockNode(myNode)) {
            return ChildAttributes(Indent.getNormalIndent(), null)
        }

        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun isLeaf(): Boolean = node.firstChildNode == null
}