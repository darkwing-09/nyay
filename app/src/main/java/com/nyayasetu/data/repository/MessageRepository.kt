package com.nyayasetu.data.repository

import com.nyayasetu.data.api.MessageApiService
import com.nyayasetu.data.models.*
import com.nyayasetu.utils.Resource
import com.nyayasetu.utils.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val apiService: MessageApiService
) {
    fun getConversations(): Flow<Resource<List<Conversation>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.getConversations()))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun createConversationWithLawyer(handle: String, initialMessage: String): Flow<Resource<Conversation>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.createConversationWithLawyer(handle, CreateConversationRequest(initialMessage))))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun getConversationDetails(id: String): Flow<Resource<List<ChatConversationMessage>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.getConversationDetails(id)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun sendMessage(id: String, content: String): Flow<Resource<ChatConversationMessage>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.sendMessage(id, SendMessageRequest(content))))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }
}
