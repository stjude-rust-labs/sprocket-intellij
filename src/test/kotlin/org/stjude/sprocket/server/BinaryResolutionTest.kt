package org.stjude.sprocket.server

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class BinaryResolutionTest {
    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `findInPath returns null when PATH is null`() {
        val result = findInPath("sprocket", null)
        assertNull(result)
    }

    @Test
    fun `findInPath returns null when binary not found`() {
        val result = findInPath("nonexistent-binary", "/usr/bin:/usr/local/bin")
        assertNull(result)
    }

    @Test
    fun `findInPath finds executable in single directory`() {
        val binary = createExecutable("sprocket")
        val result = findInPath("sprocket", tempDir.toString())

        assertNotNull(result)
        assertEquals(binary.absolutePath, result?.absolutePath)
    }

    @Test
    fun `findInPath finds executable in first matching directory`() {
        val dir1 = tempDir.resolve("dir1").toFile().also { it.mkdirs() }
        val dir2 = tempDir.resolve("dir2").toFile().also { it.mkdirs() }

        val binary1 =
            File(dir1, "sprocket").also {
                it.createNewFile()
                it.setExecutable(true)
            }
        File(dir2, "sprocket").also {
            it.createNewFile()
            it.setExecutable(true)
        }

        val pathEnv = "${dir1.absolutePath}:${dir2.absolutePath}"
        val result = findInPath("sprocket", pathEnv)

        assertNotNull(result)
        assertEquals(binary1.absolutePath, result?.absolutePath)
    }

    @Test
    fun `findInPath skips non-executable files`() {
        val nonExecutable =
            File(tempDir.toFile(), "sprocket").also {
                it.createNewFile()
                it.setExecutable(false)
            }

        val result = findInPath("sprocket", tempDir.toString())

        assertNull(result)
    }

    @Test
    fun `findInPath skips directories with same name`() {
        File(tempDir.toFile(), "sprocket").also { it.mkdirs() }

        val result = findInPath("sprocket", tempDir.toString())

        assertNull(result)
    }

    @Test
    fun `findInPath handles empty PATH components`() {
        val binary = createExecutable("sprocket")
        val pathEnv = ":$tempDir::"

        val result = findInPath("sprocket", pathEnv)

        assertNotNull(result)
    }

    @Test
    fun `findInPath handles paths with spaces`() {
        val dirWithSpaces = tempDir.resolve("dir with spaces").toFile().also { it.mkdirs() }
        val binary =
            File(dirWithSpaces, "sprocket").also {
                it.createNewFile()
                it.setExecutable(true)
            }

        val result = findInPath("sprocket", dirWithSpaces.absolutePath)

        assertNotNull(result)
        assertEquals(binary.absolutePath, result?.absolutePath)
    }

    @Test
    fun `isValidBinary returns true for existing executable`() {
        val binary = createExecutable("sprocket")
        assertTrue(isValidBinary(binary))
    }

    @Test
    fun `isValidBinary returns false for non-existent file`() {
        val nonExistent = File(tempDir.toFile(), "nonexistent")
        assertFalse(isValidBinary(nonExistent))
    }

    @Test
    fun `isValidBinary returns false for non-executable file`() {
        val nonExecutable =
            File(tempDir.toFile(), "sprocket").also {
                it.createNewFile()
                it.setExecutable(false)
            }
        assertFalse(isValidBinary(nonExecutable))
    }

    @Test
    fun `isValidBinary returns false for directory`() {
        val directory = File(tempDir.toFile(), "sprocket").also { it.mkdirs() }
        assertFalse(isValidBinary(directory))
    }

    private fun createExecutable(name: String): File =
        File(tempDir.toFile(), name).also {
            it.createNewFile()
            it.setExecutable(true)
        }

    private fun findInPath(
        name: String,
        pathEnv: String?,
    ): File? {
        if (pathEnv == null) return null
        val pathSeparator = File.pathSeparator

        for (dir in pathEnv.split(pathSeparator)) {
            if (dir.isBlank()) continue
            val file = File(dir, name)
            if (file.exists() && file.isFile && file.canExecute()) {
                return file
            }
        }
        return null
    }

    private fun isValidBinary(file: File): Boolean = file.exists() && file.isFile && file.canExecute()
}
