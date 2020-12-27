package com.nelipa.moviedatabase.network

import com.nelipa.moviedatabase.models.ResponseError
import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ResponseHandler {

    fun handleException(e: Exception): ResponseError {
        return when (e) {
            is HttpException -> ResponseError(getErrorMessage(e.code()))
            is SocketTimeoutException -> ResponseError(getErrorMessage(408))
            else -> ResponseError(getErrorMessage(Int.MAX_VALUE))
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            408 -> "Timeout"
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }
}