package org.intellij.alice.server.model


data class AliceResponce(val response: Response, val session: ResponseSession, val version: String)

data class Response(val text: String, val tts: String, val buttons: List<Button>, val end_session: Boolean) {
    constructor(text: String) : this(text, text, listOf(), true)
}

data class Button(val title: String, val url: String, val hide: Boolean)
data class ResponseSession(val session_id: String, val message_id: Long, val user_id: String) {
    constructor(session: Session) : this(session.session_id, session.message_id, session.user_id)
}