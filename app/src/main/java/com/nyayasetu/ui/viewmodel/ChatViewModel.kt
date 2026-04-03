package com.nyayasetu.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.nyayasetu.data.models.ChatMessage
import com.nyayasetu.data.models.ChatRequest
import com.nyayasetu.data.repository.ChatRepository
import com.nyayasetu.utils.Resource
import com.nyayasetu.data.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val tokenManager: TokenManager
) : BaseViewModel<Unit>() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun sendMessage(question: String) {
        val trimmed = question.trim()
        if (trimmed.isEmpty()) return
        
        val safeQuestion = if (trimmed.length > 500) trimmed.take(500) else trimmed

        val currentHistory = _messages.value
        val historyToSend = currentHistory.takeLast(10)
        
        val userMsg = ChatMessage(role = "user", content = safeQuestion)
        _messages.value = currentHistory + userMsg

        val request = ChatRequest(
            question = safeQuestion,
            language = "en",
            history = historyToSend,
            user_id = tokenManager.getUserId() ?: "unknown_user"
        )

        viewModelScope.launch {
            chatRepository.queryChat(request).collect { result ->
                when (result) {
                    is Resource.Loading -> setLoading()
                    is Resource.Success -> {
                        setSuccess(Unit)
                        val assistantMsg = ChatMessage(role = "assistant", content = result.data.answer)
                        _messages.value = _messages.value + assistantMsg
                        resetState() // Go back to idle when done
                    }
                    is Resource.Error -> setError(result.message)
                    is Resource.Idle -> {}
                }
            }
        }
    }
    
    fun clearError() {
        resetState()
    }
}
