package com.sbfirebase.kiossku.domain.apiresponse

sealed class AuthorizedApiResponse<T> (
    val data : T? = null ,
    val  errorMessage : String? = null,
    val errorCode : Int? = null
){
    class Unauthorized<T> : AuthorizedApiResponse<T>()
    class Success<T>(data : T? = null) : AuthorizedApiResponse<T>(data)
    class Failure<T>(
        errorMessage : String? = null,
        errorCode : Int? = null
    ) : AuthorizedApiResponse<T>(
        errorMessage = errorMessage,
        errorCode = errorCode
    )
    class Loading<T> : AuthorizedApiResponse<T>()
}
