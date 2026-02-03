package org.stjude.sprocket

import com.intellij.ide.ApplicationInitializedListener
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.plugins.textmate.TextMateService

class WdlTextMatePreloader : ApplicationInitializedListener {
    override suspend fun execute(asyncScope: CoroutineScope) {
        TextMateService.getInstance().reloadEnabledBundles()
    }
}
