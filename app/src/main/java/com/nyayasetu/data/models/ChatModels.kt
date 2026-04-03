package com.nyayasetu.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val role: String,
    val content: String
)

@Serializable
data class ChatRequest(
    val question: String,
    val language: String = "en",
    val history: List<ChatMessage> = emptyList(),
    val user_id: String
)

@Serializable
data class ChatResponse(
    val answer: String,
    val sources: List<String> = emptyList(),
    val disclaimer: String = ""
)
