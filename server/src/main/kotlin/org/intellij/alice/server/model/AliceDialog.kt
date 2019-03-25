package org.intellij.alice.server.model


data class AliceDialog(val meta: Meta, val request: Request, val session: Session)

data class Meta(val locale: String, val timezone: String, val client_id: String)
data class Request(val command: String, val original_utterance: String, val type: String)
