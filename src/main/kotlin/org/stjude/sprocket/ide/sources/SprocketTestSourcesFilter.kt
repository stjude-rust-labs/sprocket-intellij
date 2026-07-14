package org.stjude.sprocket.ide.sources

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.openapi.vfs.VirtualFile
import org.stjude.sprocket.lang.WdlFileType

private val TEST_DEFINITION_EXTENSIONS = setOf("yaml", "yml")
private const val TEST_DIRECTORY_NAME = "test"

/**
 * Filter for Sprocket test definitions.
 *
 * A Sprocket test directory is only valid if:
 *
 * 1. It has the name `test`
 * 2. It has sibling `.wdl` files
 *
 * A standalone test definition is only valid if:
 *
 * 1. It has the same name as a WDL file in the same directory (e.g. foo.wdl and foo.yaml)
 * 2. It has the extension `.yaml` or `.yml`
 */
class SprocketTestSourcesFilter : TestSourcesFilter() {
    override fun isTestSource(
        file: VirtualFile,
        project: Project,
    ): Boolean {
        if (file.isDirectory) {
            return isSprocketTestDir(file)
        }

        if (file.extension !in TEST_DEFINITION_EXTENSIONS) return false

        val parent = file.parent ?: return false
        if (isSprocketTestDir(parent)) {
            return true
        }

        val baseName = file.nameWithoutExtension

        // We'll just ignore `<name>.{yaml,yml}` files that don't have an accompanying
        // WDL file of the same name.
        val targetWdl = parent.findChild("$baseName." + WdlFileType.defaultExtension)
        return targetWdl != null && !targetWdl.isDirectory
    }

    /**
     * Check if `file` is a Sprocket test directory.
     */
    private fun isSprocketTestDir(file: VirtualFile): Boolean {
        if (!file.isDirectory || file.name != TEST_DIRECTORY_NAME) {
            return false
        }

        val parent = file.parent ?: return false
        return parent.children.any { it.extension == WdlFileType.defaultExtension }
    }
}
