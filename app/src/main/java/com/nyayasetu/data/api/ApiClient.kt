package com.nyayasetu.data.api

import okhttp3.Interceptor

object ApiClient {
    const val BASE_URL = "https://abhishek785-nyaya-setu.hf.space"

    val headerInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }
}
