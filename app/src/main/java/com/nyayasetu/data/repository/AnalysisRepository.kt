package com.nyayasetu.data.repository

import com.nyayasetu.data.api.AnalysisApiService
import com.nyayasetu.data.models.*
import com.nyayasetu.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.nyayasetu.utils.ErrorHandler
import javax.inject.Inject

class AnalysisRepository @Inject constructor(
    private val apiService: AnalysisApiService
) {
    fun analyzeCase(request: CaseAnalysisRequest): Flow<Resource<AnalysisResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.analyzeCase(request)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun analyzeStrength(request: CaseStrengthRequest): Flow<Resource<AnalysisResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.analyzeStrength(request)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun generateDraft(request: DraftRequest): Flow<Resource<AnalysisResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.generateDraft(request)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun analyzeFir(request: FirAnalysisRequest): Flow<Resource<AnalysisResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.analyzeFir(request)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }
}
