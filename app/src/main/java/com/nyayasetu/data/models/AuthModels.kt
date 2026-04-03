package com.nyayasetu.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val access_token: String,
    val token_type: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val full_name: String,
    val password: String,
    val role: String,
    val professional_id: String = "",
    val organization: String = "",
    val city: String = "",
    val preferred_language: String = "en"
)

@Serializable
data class User(
    val email: String,
    val full_name: String,
    val role: String,
    val professional_id: String? = null,
    val organization: String? = null,
    val city: String? = null,
    val preferred_language: String? = null
)
