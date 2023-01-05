package com.sbfirebase.kiossku.domain.apiresponse

sealed class RefreshTokenApiResponse{
    class Success(val token : String) : RefreshTokenApiResponse()

    object InternetFail : RefreshTokenApiResponse()

    object Unauthorized : RefreshTokenApiResponse()
}