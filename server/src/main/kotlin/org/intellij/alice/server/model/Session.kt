package org.intellij.alice.server.model


data class Session(val new: Boolean, val message_id: Long, val session_id: String, val skill_id: String, val user_id: String)