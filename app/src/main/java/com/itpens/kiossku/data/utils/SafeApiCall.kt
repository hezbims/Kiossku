package com.itpens.kiossku.data.utils

import com.itpens.kiossku.data.model.ApiError
import com.itpens.kiossku.domain.model.ResultWrapper
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> Response<T>
): ResultWrapper<T, ApiError> {

    return try {
        val response = apiCall()

        if (response.isSuccessful) {
            response.body()?.let {
                ResultWrapper.Success(it)
            } ?: ResultWrapper.Failure(
                ApiError.Unknown(
                    IllegalStateException("Empty response body")
                )
            )
        }
        else {
            ResultWrapper.Failure(
                ApiError.Http(
                    statusCode = response.code(),
                    body = response.errorBody()
                )
            )
        }

    }
    catch (_: SocketTimeoutException) {
        ResultWrapper.Failure(ApiError.Timeout)
    }
    catch (_: IOException){
        ResultWrapper.Failure(ApiError.Network)
    }
    catch (e: Exception) {
        ResultWrapper.Failure(ApiError.Unknown(e))
    }
}