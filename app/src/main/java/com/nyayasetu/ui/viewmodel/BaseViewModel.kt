package com.nyayasetu.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nyayasetu.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<T> : ViewModel() {
    private val _uiState = MutableStateFlow<Resource<T>>(Resource.Idle())
    val uiState: StateFlow<Resource<T>> = _uiState.asStateFlow()

    protected fun setLoading() {
        _uiState.value = Resource.Loading()
    }

    protected fun setSuccess(data: T) {
        _uiState.value = Resource.Success(data)
    }

    protected fun setError(message: String) {
        _uiState.value = Resource.Error(message)
    }

    fun resetState() {
        _uiState.value = Resource.Idle()
    }
}
