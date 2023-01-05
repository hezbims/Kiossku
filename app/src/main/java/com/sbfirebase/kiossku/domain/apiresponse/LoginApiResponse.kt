package com.sbfirebase.kiossku.domain.apiresponse

import com.sbfirebase.kiossku.data.model.login.LoginData

sealed class LoginApiResponse{
    class  Success(val loginData : LoginData) : LoginApiResponse()
    object Failed : LoginApiResponse()
}
