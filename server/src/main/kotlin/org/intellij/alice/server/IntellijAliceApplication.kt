package org.intellij.alice.server

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.request.header
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.consumeEach
import org.intellij.alice.server.connection.AliceTcpServer
import org.intellij.alice.server.connection.processName
import org.intellij.alice.server.handler.AlicePhraseHandler
import org.intellij.alice.server.model.*
import org.slf4j.LoggerFactory
import java.text.DateFormat

fun Application.main() {
    IntellijAliceApplication().apply { main() }
}


class IntellijAliceApplication {
    private val logger = LoggerFactory.getLogger(IntellijAliceApplication::class.java)

    private val server = AliceTcpServer()

    private val handler = AlicePhraseHandler(server)

    fun Application.main() {
        install(CallLogging)
        install(WebSockets) {
            pingPeriod = java.time.Duration.ofMinutes(5)
        }
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }

        routing {
            webSocket {
                val password = call.request.header("password").orEmpty()
                val name = call.request.header("name").processName()

                name?.let {
                    val userInfo = AliceUserInfo(name, this)
                    try {
                        server.userJoin(AliceUserInfo(name, this))
                        server.sendMessage(name, "Connection successful")

                        incoming.consumeEach { frame ->
                            when (frame) {
                                is Frame.Text -> {
                                    logger.info("Income message: [${frame.readText()}]")
                                }
                                else -> logger.info("Message type error")
                            }
                        }
                    } finally {
                        server.userLeft(userInfo)
                    }
                } ?: run {
                    logger.error("attempt to connect with incorrect headers")
                }
            }

            post("/alice-webhook"){
                val dialog = call.receive<AliceDialog>()
                val isInvoked = handler.handle(dialog)

                val responseText = if(isInvoked){
                    "Команда ${dialog.request.command} успешно выполнена"
                } else
                    "Команда ${dialog.request.command} не распознана"
                call.respond(
                    HttpStatusCode.OK,
                    AliceResponce(Response(responseText), ResponseSession(dialog.session), "1.0")
                )
            }
        }

    }
}