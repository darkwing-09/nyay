package com.nyayasetu.data.repository

import com.nyayasetu.utils.ErrorHandler
import com.nyayasetu.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseRepository(
    private val ioDispatcher: CoroutineDispatcher,
) {
    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> T,
    ): Resource<T> = withContext(ioDispatcher) {
        try {
            Resource.Success(apiCall())
        } catch (throwable: Throwable) {
            Resource.Error(ErrorHandler.handleException(throwable))
        }
    }
}
