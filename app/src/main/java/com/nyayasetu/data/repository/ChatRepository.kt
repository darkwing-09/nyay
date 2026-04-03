package com.nyayasetu.data.repository

import com.nyayasetu.data.api.ChatApiService
import com.nyayasetu.data.models.ChatRequest
import com.nyayasetu.data.models.ChatResponse
import com.nyayasetu.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.nyayasetu.utils.ErrorHandler
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val apiService: ChatApiService
) {
    fun queryChat(request: ChatRequest): Flow<Resource<ChatResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.queryChat(request)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }
}
