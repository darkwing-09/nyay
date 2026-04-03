package com.nyayasetu.data.repository

import com.nyayasetu.data.api.FirApiService
import com.nyayasetu.data.models.FirRequest
import com.nyayasetu.data.models.FirPreviewResponse
import com.nyayasetu.data.models.FirCreateResponse
import com.nyayasetu.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.nyayasetu.utils.ErrorHandler
import javax.inject.Inject

class FirRepository @Inject constructor(
    private val apiService: FirApiService
) {
    fun createFir(request: FirRequest): Flow<Resource<FirCreateResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.createFir(request)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun previewFir(request: FirRequest): Flow<Resource<FirPreviewResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.previewFir(request)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }
}
