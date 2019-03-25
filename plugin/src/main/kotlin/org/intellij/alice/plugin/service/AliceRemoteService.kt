package org.intellij.alice.plugin.service

interface AliceRemoteService {
    fun connect(name: String, password: String)

    fun close()

}