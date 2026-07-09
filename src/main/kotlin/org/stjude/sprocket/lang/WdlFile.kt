package org.stjude.sprocket.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class WdlFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, WdlLanguage) {

    override fun getFileType() = WdlFileType

    override fun toString() = "WDL File"
}
