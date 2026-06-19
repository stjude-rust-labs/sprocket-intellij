package org.stjude.sprocket.ide.execution.test

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import org.stjude.sprocket.lang.WdlFileType

class SprocketTestRunConfigurationProducer : LazyRunConfigurationProducer<SprocketTestRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory {
        return SprocketTestConfigurationType.getInstance().factory
    }

    override fun setupConfigurationFromContext(
        configuration: SprocketTestRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val element = context.psiLocation ?: return false

        val target = YamlUtils.resolveTestTarget(element) ?: return false
        val virtualFile = element.containingFile?.virtualFile ?: return false

        configuration.name = "Test `${target.name}`"
        configuration.sourcePath = associatedWdlFilePath(virtualFile) ?: return false
        configuration.includeTags = target.name

        sourceElement.set(element)
        return true
    }

    override fun isConfigurationFromContext(
        configuration: SprocketTestRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val element = context.psiLocation ?: return false

        val target = YamlUtils.resolveTestTarget(element) ?: return false
        val virtualFile = element.containingFile?.virtualFile ?: return false
        val wdlPath = associatedWdlFilePath(virtualFile) ?: return false

        return configuration.sourcePath == wdlPath && configuration.includeTags == target.name
    }

    fun associatedWdlFilePath(file: VirtualFile): String? {
        val expectedWdl = file.nameWithoutExtension + ".${WdlFileType.defaultExtension}"
        val parent = file.parent ?: return null

        val inTestDir = parent.isDirectory && parent.name == "test"
        val wdlDir = if (inTestDir) {
            val grandparent = file.parent.parent ?: return null
            grandparent.canonicalPath
        } else {
            parent.canonicalPath
        } ?: return null

        return wdlDir + VfsUtilCore.VFS_SEPARATOR + expectedWdl
    }
}