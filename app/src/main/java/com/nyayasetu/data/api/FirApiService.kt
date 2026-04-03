package com.nyayasetu.data.api

import com.nyayasetu.data.models.FirRequest
import com.nyayasetu.data.models.FirPreviewResponse
import com.nyayasetu.data.models.FirCreateResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FirApiService {
    @POST("/api/v1/fir/manual")
    suspend fun createFir(@Body request: FirRequest): FirCreateResponse

    @POST("/api/v1/fir/manual/preview")
    suspend fun previewFir(@Body request: FirRequest): FirPreviewResponse
}
