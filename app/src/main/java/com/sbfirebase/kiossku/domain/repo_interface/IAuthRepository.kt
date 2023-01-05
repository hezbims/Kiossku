package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.login.SuccessfulLoginResponse
import com.sbfirebase.kiossku.data.model.logout.LogoutResponse
import com.sbfirebase.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import retrofit2.Response

interface IAuthRepository {
    suspend fun login(email : String , password : String) : Response<SuccessfulLoginResponse>

    suspend fun refreshToken() : Response<SuccessfulRefreshTokenResponse>

    suspend fun  logout() : Response<LogoutResponse>
}