package org.intellij.alice.plugin.service.impl

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.ApplicationManager
import org.intellij.alice.plugin.IntellijActionId
import org.intellij.alice.plugin.service.ActionCallService

class DefaultActionCallService : ActionCallService {
    private val actionManager = ActionManager.getInstance()
    private val dataManager = DataManager.getInstance()
    private val application = ApplicationManager.getApplication()

    override fun callActionById(id: IntellijActionId) {
        val action = actionManager.getAction(id.name) ?: return
        val actionFactory = { dataContext: DataContext ->
            AnActionEvent(
                null,
                dataContext,
                ActionPlaces.UNKNOWN,
                Presentation(),
                actionManager,
                0
            )
        }
        dataManager.dataContextFromFocusAsync.onSuccess { dataContext ->
            application.invokeLater {
                action.actionPerformed(actionFactory(dataContext))
            }
        }

    }
}