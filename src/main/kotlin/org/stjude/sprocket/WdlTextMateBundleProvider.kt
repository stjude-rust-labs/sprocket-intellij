package org.stjude.sprocket

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import java.nio.file.Files

class WdlTextMateBundleProvider : TextMateBundleProvider {
    override fun getBundles(): List<TextMateBundleProvider.PluginBundle> {
        val plugin = PluginManagerCore.getPlugin(PluginId.getId("org.stjude.sprocket"))
            ?: return emptyList()

        val bundlePath = plugin.pluginPath.resolve("syntaxes")
        if (!Files.exists(bundlePath)) {
            return emptyList()
        }

        return listOf(
            TextMateBundleProvider.PluginBundle("WDL", bundlePath)
        )
    }
}
