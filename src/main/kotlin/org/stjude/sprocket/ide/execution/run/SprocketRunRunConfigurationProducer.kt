package org.stjude.sprocket.ide.execution.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.stjude.sprocket.lang.psi.WdlTokenTypes
import org.stjude.sprocket.lang.psi.impl.WdlTaskImpl
import org.stjude.sprocket.lang.psi.impl.WdlWorkflowImpl

class SprocketRunRunConfigurationProducer : LazyRunConfigurationProducer<SprocketRunRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory {
        return SprocketRunConfigurationType.getInstance().factory
    }

    override fun setupConfigurationFromContext(
        configuration: SprocketRunRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val element = context.psiLocation ?: return false
        if (element.elementType != WdlTokenTypes.IDENTIFIER) return false
        if (element.parent !is WdlTaskImpl || element.parent !is WdlWorkflowImpl) return false

        val target = element.text
        val virtualFile = element.containingFile?.virtualFile ?: return false
        configuration.name = "Run `${target}`"
        configuration.sourcePath = virtualFile.path
        configuration.target = target

        sourceElement.set(element)
        return true
    }

    override fun isConfigurationFromContext(
        configuration: SprocketRunRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val element = context.psiLocation ?: return false
        val virtualFile = element.containingFile?.virtualFile ?: return false

        return configuration.sourcePath == virtualFile.path
    }
}