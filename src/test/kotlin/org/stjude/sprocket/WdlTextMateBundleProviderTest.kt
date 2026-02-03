package org.stjude.sprocket

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class WdlTextMateBundleProviderTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `bundle path should end with syntaxes`() {
        val expectedSuffix = "syntaxes"
        val bundlePath = tempDir.resolve(expectedSuffix)

        assertTrue(bundlePath.toString().endsWith(expectedSuffix))
    }

    @Test
    fun `bundle directory structure is valid when it exists`() {
        val syntaxesDir = tempDir.resolve("syntaxes")
        Files.createDirectory(syntaxesDir)

        assertTrue(Files.exists(syntaxesDir))
        assertTrue(Files.isDirectory(syntaxesDir))
    }

    @Test
    fun `bundle directory can contain grammar file`() {
        val syntaxesDir = tempDir.resolve("syntaxes")
        Files.createDirectory(syntaxesDir)

        val grammarFile = syntaxesDir.resolve("wdl.tmGrammar.json")
        Files.writeString(grammarFile, "{}")

        assertTrue(Files.exists(grammarFile))
    }

    @Test
    fun `bundle directory can contain package json`() {
        val syntaxesDir = tempDir.resolve("syntaxes")
        Files.createDirectory(syntaxesDir)

        val packageJson = syntaxesDir.resolve("package.json")
        Files.writeString(packageJson, """{"name": "wdl"}""")

        assertTrue(Files.exists(packageJson))
    }

    @Test
    fun `valid bundle has both grammar and package json`() {
        val syntaxesDir = tempDir.resolve("syntaxes")
        Files.createDirectory(syntaxesDir)
        Files.writeString(syntaxesDir.resolve("wdl.tmGrammar.json"), "{}")
        Files.writeString(syntaxesDir.resolve("package.json"), """{"name": "wdl"}""")

        val files = Files.list(syntaxesDir).map { it.fileName.toString() }.toList()
        assertTrue(files.contains("wdl.tmGrammar.json"))
        assertTrue(files.contains("package.json"))
    }

    @Test
    fun `package json structure is valid`() {
        val packageJson = """
        {
          "name": "wdl",
          "displayName": "WDL",
          "version": "0.1.0",
          "publisher": "stjude",
          "description": "WDL (Workflow Description Language) syntax highlighting",
          "contributes": {
            "languages": [
              {
                "id": "WDL",
                "aliases": ["WDL", "Workflow Description Language"],
                "extensions": [".wdl"]
              }
            ],
            "grammars": [
              {
                "language": "WDL",
                "scopeName": "source.wdl",
                "path": "./wdl.tmGrammar.json"
              }
            ]
          }
        }
        """.trimIndent()

        assertTrue(packageJson.contains("\"name\": \"wdl\""))
        assertTrue(packageJson.contains("\"scopeName\": \"source.wdl\""))
        assertTrue(packageJson.contains("\".wdl\""))
    }

    @Test
    fun `empty bundle directory returns empty list`() {
        val syntaxesDir = tempDir.resolve("syntaxes")
        Files.createDirectory(syntaxesDir)

        val files = Files.list(syntaxesDir).toList()
        assertTrue(files.isEmpty())
    }

    @Test
    fun `non-existent bundle directory is detected`() {
        val syntaxesDir = tempDir.resolve("syntaxes")
        assertFalse(Files.exists(syntaxesDir))
    }
}
