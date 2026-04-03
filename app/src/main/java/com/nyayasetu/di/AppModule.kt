package com.nyayasetu.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nyayasetu.data.TokenManager
import com.nyayasetu.data.api.ApiClient
import com.nyayasetu.data.api.AuthApiService
import com.nyayasetu.data.api.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApiService(retrofit: Retrofit): com.nyayasetu.data.api.ChatApiService {
        return retrofit.create(com.nyayasetu.data.api.ChatApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFirApiService(retrofit: Retrofit): com.nyayasetu.data.api.FirApiService {
        return retrofit.create(com.nyayasetu.data.api.FirApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnalysisApiService(retrofit: Retrofit): com.nyayasetu.data.api.AnalysisApiService {
        return retrofit.create(com.nyayasetu.data.api.AnalysisApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAdvancedFirApiService(retrofit: Retrofit): com.nyayasetu.data.api.AdvancedFirApiService {
        return retrofit.create(com.nyayasetu.data.api.AdvancedFirApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLawyerApiService(retrofit: Retrofit): com.nyayasetu.data.api.LawyerApiService {
        return retrofit.create(com.nyayasetu.data.api.LawyerApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMessageApiService(retrofit: Retrofit): com.nyayasetu.data.api.MessageApiService {
        return retrofit.create(com.nyayasetu.data.api.MessageApiService::class.java)
    }
}
