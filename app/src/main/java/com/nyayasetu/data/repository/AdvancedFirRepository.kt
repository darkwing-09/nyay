package com.nyayasetu.data.repository

import com.nyayasetu.data.api.AdvancedFirApiService
import com.nyayasetu.data.models.AdvancedFirResponse
import com.nyayasetu.data.models.JurisdictionRequest
import com.nyayasetu.data.models.SectionPredictionRequest
import com.nyayasetu.utils.ErrorHandler
import com.nyayasetu.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class AdvancedFirRepository @Inject constructor(
    private val apiService: AdvancedFirApiService
) {
    fun uploadFir(
        complaintFile: MultipartBody.Part,
        policeStation: MultipartBody.Part,
        draftRole: MultipartBody.Part,
        language: MultipartBody.Part,
        userId: MultipartBody.Part
    ): Flow<Resource<AdvancedFirResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.uploadFir(complaintFile, policeStation, draftRole, language, userId)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun uploadVoiceFir(
        audioFile: MultipartBody.Part,
        transcriptText: MultipartBody.Part?,
        policeStation: MultipartBody.Part,
        complainantName: MultipartBody.Part,
        draftRole: MultipartBody.Part,
        language: MultipartBody.Part,
        userId: MultipartBody.Part
    ): Flow<Resource<AdvancedFirResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.uploadVoiceFir(audioFile, transcriptText, policeStation, complainantName, draftRole, language, userId)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun predictSections(request: SectionPredictionRequest): Flow<Resource<AdvancedFirResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.predictSections(request)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun predictJurisdiction(request: JurisdictionRequest): Flow<Resource<AdvancedFirResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.predictJurisdiction(request)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun checkCompleteness(request: com.nyayasetu.data.models.FirRequest): Flow<Resource<AdvancedFirResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.checkCompleteness(request)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun analyzeEvidence(evidenceFiles: List<MultipartBody.Part>): Flow<Resource<AdvancedFirResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.analyzeEvidence(evidenceFiles)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }
}
