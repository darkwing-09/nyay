package com.nyayasetu.data.api

import com.nyayasetu.data.models.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AnalysisApiService {
    @POST("/api/v1/analysis/case")
    suspend fun analyzeCase(@Body request: CaseAnalysisRequest): AnalysisResponse

    @POST("/api/v1/analysis/strength")
    suspend fun analyzeStrength(@Body request: CaseStrengthRequest): AnalysisResponse

    @POST("/api/v1/analysis/draft")
    suspend fun generateDraft(@Body request: DraftRequest): AnalysisResponse

    @POST("/api/v1/analysis/fir")
    suspend fun analyzeFir(@Body request: FirAnalysisRequest): AnalysisResponse
}
