package com.nyayasetu.data.api

import com.nyayasetu.data.models.AdvancedFirResponse
import com.nyayasetu.data.models.JurisdictionRequest
import com.nyayasetu.data.models.SectionPredictionRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AdvancedFirApiService {

    @Multipart
    @POST("/api/v1/fir/upload")
    suspend fun uploadFir(
        @Part complaint_file: MultipartBody.Part,
        @Part police_station: MultipartBody.Part,
        @Part draft_role: MultipartBody.Part,
        @Part language: MultipartBody.Part,
        @Part user_id: MultipartBody.Part
    ): AdvancedFirResponse

    @Multipart
    @POST("/api/v1/fir/voice")
    suspend fun uploadVoiceFir(
        @Part audio_file: MultipartBody.Part,
        @Part transcript_text: MultipartBody.Part?,
        @Part police_station: MultipartBody.Part,
        @Part complainant_name: MultipartBody.Part,
        @Part draft_role: MultipartBody.Part,
        @Part language: MultipartBody.Part,
        @Part user_id: MultipartBody.Part
    ): AdvancedFirResponse

    @POST("/api/v1/fir/sections/predict")
    suspend fun predictSections(@Body request: SectionPredictionRequest): AdvancedFirResponse

    @POST("/api/v1/fir/jurisdiction")
    suspend fun predictJurisdiction(@Body request: JurisdictionRequest): AdvancedFirResponse

    @POST("/api/v1/fir/completeness")
    suspend fun checkCompleteness(@Body request: com.nyayasetu.data.models.FirRequest): AdvancedFirResponse

    @Multipart
    @POST("/api/v1/fir/evidence/analyze")
    suspend fun analyzeEvidence(
        @Part evidence_files: List<MultipartBody.Part>
    ): AdvancedFirResponse
}
