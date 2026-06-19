package org.stjude.sprocket.ide.execution.test

import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YAMLSequenceItem

/**
 * Utilities for interacting with Sprocket test definition YAMLs.
 */
object YamlUtils {

    /**
     * A Sprocket test target.
     */
    sealed class TestTarget {
        abstract val name: String

        /**
         * Run *all* tests associated with this entrypoint.
         */
        data class Entrypoint(override val name: String) : TestTarget()

        /**
         * Run a single test under an entrypoint.
         */
        data class Test(override val name: String) : TestTarget()
    }

    /**
     * Resolves `element` to a YAML `TestTarget` if it belongs to one.
     */
    fun resolveTestTarget(element: PsiElement): TestTarget? {
        val virtualFile = element.containingFile?.virtualFile ?: return null
        if (!TestSourcesFilter.isTestSources(virtualFile, element.project)) return null

        val currentKV = PsiTreeUtil.getParentOfType(element, YAMLKeyValue::class.java, false) ?: return null
        if (isTopLevelKV(currentKV)) {
            return TestTarget.Entrypoint(currentKV.keyText)
        }

        // foo_entrypoint:
        //   - name: "some_test"

        // "name" denotes the start of a test entry
        if (currentKV.keyText != "name") return null

        // Then it should be in a sequence immediately under the top-level entrypoint
        val mapping = currentKV.parent as? YAMLMapping ?: return null
        val sequenceItem = mapping.parent as? YAMLSequenceItem ?: return null
        val sequence = sequenceItem.parent as? YAMLSequence ?: return null
        if (isTopLevelKV(sequence.parent) && currentKV.valueText.isNotBlank()) {
            return TestTarget.Test(currentKV.valueText)
        }

        return null
    }

    private fun isTopLevelKV(element: PsiElement): Boolean {
        return element.parent is YAMLMapping && element.parent?.parent is YAMLDocument
    }
}