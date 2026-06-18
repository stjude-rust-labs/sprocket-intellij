package org.stjude.sprocket.ide.typing

import com.intellij.lang.Commenter

class WdlCommenter : Commenter {
    override fun getLineCommentPrefix(): String = "#"
    override fun getBlockCommentPrefix(): String? = null
    override fun getBlockCommentSuffix(): String? = null
    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}