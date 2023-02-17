package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.login.LoginDto
import com.sbfirebase.kiossku.data.model.logout.LogoutResponse
import com.sbfirebase.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IAuthRepository {
    suspend fun login(email : String , password : String) : Flow<ApiResponse<LoginDto>>

    suspend fun register(
        registerBody: RegisterPost
    ) : Flow<ApiResponse<Nothing>>

    suspend fun sendEmailTokenConfirmation(email : String) : Flow<ApiResponse<Nothing>>

    suspend fun confirmEmail(token : String) : Flow<ApiResponse<Nothing>>

    suspend fun refreshToken() : Response<SuccessfulRefreshTokenResponse>

    suspend fun  logout() : Response<LogoutResponse>
}