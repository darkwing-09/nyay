package com.nyayasetu.data.api

import com.nyayasetu.data.models.ChatRequest
import com.nyayasetu.data.models.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {
    @POST("/api/v1/chat/query")
    suspend fun queryChat(@Body request: ChatRequest): ChatResponse
}
