package org.intellij.alice.server.model

import io.ktor.http.cio.websocket.WebSocketSession

data class AliceUserInfo(val name:String, val connection: WebSocketSession){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as AliceUserInfo
        return other.name == name
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}