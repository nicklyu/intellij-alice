package org.intellij.alice.plugin.component

import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.intellij.alice.plugin.service.AliceRemoteService

class AliceProjectComponent(private val project: Project) : ProjectComponent {
    override fun initComponent() {
        project.service<AliceRemoteService>().connect("test", "test")
    }

    override fun disposeComponent() {
        project.service<AliceRemoteService>().close()
    }
}