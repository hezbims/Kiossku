package com.itpens.kiossku.data.model

import com.itpens.kiossku.domain.model.ExternalSystemError
import okhttp3.ResponseBody

sealed class ApiError {
    object Timeout : ApiError()
    object Network : ApiError()
    data class Unknown(val ex: Exception) : ApiError()
    data class Http(val body : ResponseBody?, val statusCode: Int) : ApiError()

    fun toDomain() : ExternalSystemError {
        return when(this){
            is Http -> getExternalSystemErrorModelFromStatusCode(this.statusCode)
            Network -> ExternalSystemError.NetworkIssue
            Timeout -> ExternalSystemError.Timeout
            is Unknown -> ExternalSystemError.Unexpected
        }
    }

    private fun getExternalSystemErrorModelFromStatusCode(statusCode: Int) : ExternalSystemError {
        return when(statusCode) {
            503, 429 -> ExternalSystemError.ServiceUnavailable
            401 -> ExternalSystemError.UnauthorizedAccess
            else -> ExternalSystemError.Unexpected
        }
    }
}