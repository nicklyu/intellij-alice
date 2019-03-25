package org.intellij.alice.server.model

import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

data class AliceUserInfo(val name:String, val connection: WebSocketSession){

    private val timer = Timer().apply {
        this.schedule(20000, 20000){
            GlobalScope.launch {
                connection.send(Frame.Text("ping"))
            }
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as AliceUserInfo
        return other.name == name
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun dispose(){
        timer.cancel()
    }
}