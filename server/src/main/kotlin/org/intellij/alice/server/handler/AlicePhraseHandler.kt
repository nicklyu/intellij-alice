package org.intellij.alice.server.handler

import org.intellij.alice.server.connection.AliceTcpServer
import org.intellij.alice.server.model.AliceDialog
import org.slf4j.LoggerFactory

class AlicePhraseHandler(private val server: AliceTcpServer) {
    private val logger = LoggerFactory.getLogger(AlicePhraseHandler::class.java)


    suspend fun handle(dialog: AliceDialog){
        logger.info("Trying parse phrase [${dialog.request.command}] ...")
        val command = dialog.request.command
        val commandInfo = CommandMatcher.findCommand(command) ?: return
        logger.info("Command [${dialog.request.command}] matched to [${commandInfo.first}] with ${commandInfo.second} accuracy")

        val message = commandInfo.first
        server.broadcast(message)
    }
}