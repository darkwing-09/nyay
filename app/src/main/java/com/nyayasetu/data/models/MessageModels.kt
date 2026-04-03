package com.nyayasetu.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Conversation(
    val id: String = "",
    val lawyer_handle: String = "",
    val lawyer_name: String = "",
    val last_message: String = "",
    val unread_count: Int = 0,
    val updated_at: String = ""
)

@Serializable
data class ChatConversationMessage(
    val id: String = "",
    val sender_id: String = "",
    val receiver_id: String = "",
    val content: String = "",
    val timestamp: String = "",
    val is_lawyer: Boolean = false
)

@Serializable
data class SendMessageRequest(
    val content: String
)

@Serializable
data class MessageGenericResponse(
    val status: String? = null,
    val message: String? = null,
    val data: JsonElement? = null
)

@Serializable
data class CreateConversationRequest(
    val initial_message: String
)
