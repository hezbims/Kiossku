package com.sbfirebase.kiossku.domain.apiresponse

sealed class UnAuthorizedApiResponse<T> (
    val data : T? = null ,
    val  error : String? = null
){
    class Success<T>(data : T) : UnAuthorizedApiResponse<T>(data)
    class Failure(error : String? = null) : UnAuthorizedApiResponse<Nothing>()
    object Loading : UnAuthorizedApiResponse<Nothing>()
}
