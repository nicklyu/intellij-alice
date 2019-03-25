package org.intellij.alice.plugin.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.intellij.alice.plugin.IntellijActionId

interface ActionCallService {

    fun callActionById(id: String)

    companion object {
        fun instance(project: Project) = project.service<ActionCallService>()
    }
}