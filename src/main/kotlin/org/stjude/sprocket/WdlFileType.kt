package org.stjude.sprocket

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object WdlFileType : LanguageFileType(WdlLanguage) {
    override fun getName(): String = "WDL"

    override fun getDescription(): String = "Workflow Description Language"

    override fun getDefaultExtension(): String = "wdl"

    override fun getIcon(): Icon = WdlIcons.FILE
}
