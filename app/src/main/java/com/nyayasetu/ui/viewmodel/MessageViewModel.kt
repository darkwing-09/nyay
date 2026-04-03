package com.nyayasetu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyayasetu.data.models.*
import com.nyayasetu.data.repository.MessageRepository
import com.nyayasetu.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: MessageRepository
) : ViewModel() {

    private val _conversationsState = MutableStateFlow<Resource<List<Conversation>>>(Resource.Idle())
    val conversationsState: StateFlow<Resource<List<Conversation>>> = _conversationsState.asStateFlow()

    private val _messagesState = MutableStateFlow<Resource<List<ChatConversationMessage>>>(Resource.Idle())
    val messagesState: StateFlow<Resource<List<ChatConversationMessage>>> = _messagesState.asStateFlow()

    private val _sendState = MutableStateFlow<Resource<ChatConversationMessage>>(Resource.Idle())
    val sendState: StateFlow<Resource<ChatConversationMessage>> = _sendState.asStateFlow()

    private var currentConversationId: String? = null

    fun fetchConversations() {
        if (_conversationsState.value is Resource.Loading) return
        viewModelScope.launch {
            repository.getConversations().collect { result ->
                if (result is Resource.Success) {
                    val fallbackList = result.data ?: emptyList()
                    _conversationsState.value = Resource.Success(fallbackList)
                } else {
                    _conversationsState.value = result
                }
            }
        }
    }

    fun openConversation(id: String) {
        currentConversationId = id
        _messagesState.value = Resource.Idle()
        fetchConversationDetails(id)
    }

    fun fetchConversationDetails(id: String) {
        if (_messagesState.value is Resource.Loading) return
        viewModelScope.launch {
            repository.getConversationDetails(id).collect { result ->
                if (result is Resource.Success) {
                    val messages = (result.data ?: emptyList()).takeLast(50)
                    _messagesState.value = Resource.Success(messages)
                } else {
                    _messagesState.value = result
                }
            }
        }
    }

    fun sendMessage(content: String) {
        val id = currentConversationId ?: return
        val text = content.trim()
        if (text.isEmpty()) return

        // Optimistic UI Append
        val optimisticMessage = ChatConversationMessage(
            id = "temp_${System.currentTimeMillis()}",
            content = text,
            is_lawyer = false
        )

        val currentList = (_messagesState.value as? Resource.Success)?.data ?: emptyList()
        val newList = (currentList + optimisticMessage).distinctBy { it.id }.takeLast(50)
        _messagesState.value = Resource.Success(newList)

        viewModelScope.launch {
            repository.sendMessage(id, text).collect { result ->
                _sendState.value = result
                if (result is Resource.Success) {
                    // Sync up perfectly tracking backend IDs preventing duplicates
                    fetchConversationDetails(id)
                } else if (result is Resource.Error) {
                    // Remove optimistic element safely avoiding duplicates
                    val revertList = currentList.filter { it.id != optimisticMessage.id }
                    _messagesState.value = Resource.Success(revertList)
                }
            }
        }
    }

    fun startConversation(handle: String, initialMessage: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            repository.createConversationWithLawyer(handle, initialMessage).collect { result ->
                if (result is Resource.Success) {
                    onSuccess(result.data.id)
                }
            }
        }
    }

    fun resetSendState() {
        _sendState.value = Resource.Idle()
    }
}
