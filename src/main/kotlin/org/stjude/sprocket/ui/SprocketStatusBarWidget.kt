package org.stjude.sprocket.ui

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import com.intellij.util.Consumer
import java.awt.event.MouseEvent

class SprocketStatusBarWidget(project: Project) : EditorBasedWidget(project),
    StatusBarWidget.MultipleTextValuesPresentation {

    override fun ID(): String = "SprocketStatusBar"

    override fun getTooltipText(): String = "Sprocket"

    override fun getSelectedValue(): String = "Sprocket"

    override fun getPopup(): ListPopup? {
        val actions = DefaultActionGroup().apply {
            add(RestartServerAction())
            add(OpenSettingsAction())
        }

        val component = myStatusBar?.component ?: return null
        val dataContext = DataManager.getInstance().getDataContext(component)

        return JBPopupFactory.getInstance().createActionGroupPopup(
            "Sprocket",
            actions,
            dataContext,
            JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
            true
        )
    }

    override fun getClickConsumer(): Consumer<MouseEvent>? = null

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this
}
