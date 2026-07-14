package org.stjude.sprocket.ide.todo

import com.intellij.lexer.Lexer
import com.intellij.psi.impl.cache.impl.BaseFilterLexer
import com.intellij.psi.impl.cache.impl.OccurrenceConsumer
import com.intellij.psi.impl.cache.impl.todo.LexerBasedTodoIndexer
import com.intellij.psi.search.UsageSearchContext
import org.stjude.sprocket.lang.WdlLexerAdapter
import org.stjude.sprocket.lang.WdlTokenSets

class WdlTodoIndexer : LexerBasedTodoIndexer() {
    override fun createLexer(consumer: OccurrenceConsumer): Lexer = WdlFilterLexer(consumer)
}

private class WdlFilterLexer(
    consumer: OccurrenceConsumer,
) : BaseFilterLexer(WdlLexerAdapter(), consumer) {
    override fun advance() {
        if (myDelegate.tokenType in WdlTokenSets.COMMENTS) {
            scanWordsInToken(UsageSearchContext.IN_COMMENTS.toInt(), false, false)
            advanceTodoItemCountsInToken()
        }

        myDelegate.advance()
    }
}
