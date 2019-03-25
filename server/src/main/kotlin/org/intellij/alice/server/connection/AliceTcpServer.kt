package org.intellij.alice.server.connection

import io.ktor.http.cio.websocket.Frame
import io.netty.util.internal.ConcurrentSet
import org.intellij.alice.server.model.AliceUserInfo
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

class AliceTcpServer {
    private val logger = LoggerFactory.getLogger(AliceTcpServer::class.java)

    var userCounter = AtomicInteger()

    var users = ConcurrentSet<AliceUserInfo>()

    fun userJoin(info: AliceUserInfo) {
        users.add(info)

        logger.info("User number [${userCounter.incrementAndGet()}] [${info.name}] added")
    }

    fun userLeft(info: AliceUserInfo) {
        users.remove(info)
        logger.info("User  [${info.name}] left. [${userCounter.decrementAndGet()}] users left")
    }

    suspend fun sendMessage(name: String, message: String) {
        users.filter { user -> user.name == name }.forEach { userInfo ->
            userInfo.connection.send(Frame.Text(message))
            logger.info("Message [$message] sent to [${userInfo.name}]")
        }
    }

    suspend fun broadcast(message: String) {
        users.forEach { user ->
            sendMessage(user.name, message)
        }
    }
}