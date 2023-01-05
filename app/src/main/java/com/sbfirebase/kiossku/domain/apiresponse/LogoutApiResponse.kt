package com.sbfirebase.kiossku.domain.apiresponse

sealed class LogoutApiResponse{
    object Success : LogoutApiResponse()
    object Failed : LogoutApiResponse()
}
