package org.intellij.alice.plugin.service.impl

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.*
import io.ktor.client.request.head
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.intellij.alice.plugin.service.AliceRemoteService
import java.util.logging.Logger


class DefaultAliceRemoteService : AliceRemoteService {
    private val logger = Logger.getLogger(AliceRemoteService::class.java.name)
    private val client: HttpClient = HttpClient(CIO) {
        install(WebSockets)
    }


    override fun connect(name: String, password: String) {
        GlobalScope.launch {
//            client.wssRaw(method = HttpMethod.Get, host = "intellij-alice.herokuapp.com", path = "/", request = {
            client.webSocket(method = HttpMethod.Get, path = "/", request = {
                this.headers.append("name", name)
            }) {
                this.incoming.consumeEach { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            logger.info("New message: [${frame.readText()}]")
                        }

                        else -> logger.info("Unsupported frame type")
                    }
                }
            }
        }
    }

    override fun close() {
        client.close()
    }
}