package com.nyayasetu.utils

import retrofit2.HttpException
import java.io.IOException

object ErrorHandler {
    fun handleException(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> mapHttpError(throwable.code())
            is IOException -> "Unable to reach the server. Please check your network connection."
            else -> throwable.message ?: "An unexpected error occurred."
        }
    }

    fun mapHttpError(code: Int): String {
        return when (code) {
            401 -> "Your session is unauthorized. Please sign in again."
            422 -> "The request could not be processed. Please review the input and try again."
            in 500..599 -> "The server is currently unavailable. Please try again in a moment."
            else -> "Something went wrong. Please try again."
        }
    }
}
