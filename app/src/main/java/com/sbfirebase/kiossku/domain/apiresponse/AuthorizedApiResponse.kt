package com.sbfirebase.kiossku.domain.apiresponse

sealed class AuthorizedApiResponse<T> (
    val data : T? = null ,
    val  error : String? = null
){
    class Unauthorized<T> : AuthorizedApiResponse<T>()
    class Success<T>(data : T) : AuthorizedApiResponse<T>(data)
    class Failure<T>(error : String? = null) : AuthorizedApiResponse<T>()
    class Loading<T> : AuthorizedApiResponse<T>()
}
