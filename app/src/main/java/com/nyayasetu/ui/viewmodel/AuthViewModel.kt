package com.nyayasetu.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.nyayasetu.data.models.LoginRequest
import com.nyayasetu.data.models.RegisterRequest
import com.nyayasetu.data.models.User
import com.nyayasetu.data.repository.AuthRepository
import com.nyayasetu.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : BaseViewModel<Unit>() {

    private val _userState = MutableStateFlow<Resource<User>?>(null)
    val userState: StateFlow<Resource<User>?> = _userState.asStateFlow()

    fun hasToken(): Boolean = repository.hasToken()

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            repository.login(request).collect { result ->
                when (result) {
                    is Resource.Loading -> setLoading()
                    is Resource.Success -> {
                        setSuccess(Unit)
                        getCurrentUser()
                    }
                    is Resource.Error -> setError(result.message)
                    is Resource.Idle -> {}
                }
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            repository.register(request).collect { result ->
                when (result) {
                    is Resource.Loading -> setLoading()
                    is Resource.Success -> setSuccess(Unit)
                    is Resource.Error -> setError(result.message)
                    is Resource.Idle -> {}
                }
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            repository.getCurrentUser().collect { result ->
                _userState.value = result
            }
        }
    }

    fun logout() {
        repository.logout()
        _userState.value = null
        resetState()
    }
}
