package com.nyayasetu.data.api

import com.nyayasetu.data.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Accept", "application/json")

        val contentType = originalRequest.body?.contentType()
        if (contentType == null || contentType.type != "multipart") {
            requestBuilder.header("Content-Type", "application/json")
        }

        tokenManager.getToken()?.let { token ->
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())
        
        if (response.code == 401) {
            tokenManager.clearToken()
        }
        
        return response
    }
}
