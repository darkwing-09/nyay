package com.nyayasetu.data.repository

import com.nyayasetu.data.api.LawyerApiService
import com.nyayasetu.data.models.*
import com.nyayasetu.utils.Resource
import com.nyayasetu.utils.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LawyerRepository @Inject constructor(
    private val apiService: LawyerApiService
) {
    fun getLawyers(): Flow<Resource<List<Lawyer>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.getLawyers()))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun getLawyerProfile(handle: String): Flow<Resource<LawyerProfile>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.getLawyerProfile(handle)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun followLawyer(handle: String): Flow<Resource<GenericLawyerResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.followLawyer(handle)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun getFeed(): Flow<Resource<List<FeedPost>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.getFeed()))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun createPost(content: String): Flow<Resource<GenericLawyerResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.createPost(CreatePostRequest(content))))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }

    fun likePost(postId: String): Flow<Resource<GenericLawyerResponse>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(apiService.likePost(postId)))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorHandler.handleException(e)))
        }
    }
}
