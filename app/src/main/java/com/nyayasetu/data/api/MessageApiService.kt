package com.nyayasetu.data.api

import com.nyayasetu.data.models.*
import retrofit2.http.*

interface MessageApiService {
    @GET("/messages/conversations")
    suspend fun getConversations(): List<Conversation>

    @POST("/messages/lawyer/{handle}")
    suspend fun createConversationWithLawyer(@Path("handle") handle: String, @Body request: CreateConversationRequest): Conversation

    @GET("/messages/conversations/{id}")
    suspend fun getConversationDetails(@Path("id") id: String): List<ChatConversationMessage>

    @POST("/messages/conversations/{id}/messages")
    suspend fun sendMessage(@Path("id") id: String, @Body request: SendMessageRequest): ChatConversationMessage
}
