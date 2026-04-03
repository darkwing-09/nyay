package com.nyayasetu.data.api

import com.nyayasetu.data.models.LoginRequest
import com.nyayasetu.data.models.LoginResponse
import com.nyayasetu.data.models.RegisterRequest
import com.nyayasetu.data.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @GET("/api/v1/auth/me")
    suspend fun getCurrentUser(): User
}
