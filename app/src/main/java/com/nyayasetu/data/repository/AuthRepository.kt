package com.nyayasetu.data.repository

import com.nyayasetu.data.TokenManager
import com.nyayasetu.data.api.AuthApiService
import com.nyayasetu.data.models.LoginRequest
import com.nyayasetu.data.models.RegisterRequest
import com.nyayasetu.data.models.User
import com.nyayasetu.utils.Resource
import com.nyayasetu.utils.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) {

    fun login(request: LoginRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.login(request)
            tokenManager.saveToken(response.access_token)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun register(request: RegisterRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            apiService.register(request)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun getCurrentUser(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val user = apiService.getCurrentUser()
            tokenManager.saveUserId(user.email)
            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun hasToken(): Boolean {
        return tokenManager.getToken() != null
    }

    fun logout() {
        tokenManager.clearToken()
    }
}
