package org.stjude.sprocket.lang

import com.intellij.testFramework.ParsingTestCase

class WdlParserTest : ParsingTestCase("parser", "wdl", WdlParserDefinition()) {
    override fun getTestDataPath() = System.getProperty("user.dir") + "/src/test/assets"

    fun testStruct() = doTest(true)
}