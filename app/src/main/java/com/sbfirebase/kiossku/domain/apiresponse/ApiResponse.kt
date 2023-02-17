package com.sbfirebase.kiossku.domain.apiresponse

sealed class ApiResponse<T> (
    val data : T? = null ,
    val  errorMessage : String? = null,
    val errorCode : Int? = null
){
    class Success<T>(data : T? = null) : ApiResponse<T>(data)
    class Failure<T>(
        errorMessage : String? = null,
        errorCode : Int? = null
    ) : ApiResponse<T>(
        errorMessage = errorMessage,
        errorCode = errorCode
    )
    class Loading<T> : ApiResponse<T>()
}
