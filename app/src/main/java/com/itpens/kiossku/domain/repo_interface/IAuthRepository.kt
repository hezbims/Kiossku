package com.itpens.kiossku.domain.repo_interface

import com.itpens.kiossku.data.model.auth.FailedRefreshTokenError
import com.itpens.kiossku.data.model.auth.TokenData
import com.itpens.kiossku.data.model.login.LoginDto
import com.itpens.kiossku.data.model.register.RegisterPost
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.model.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun login(email : String , password : String) : ApiResponse<LoginDto>

    suspend fun register(
        registerBody: RegisterPost
    ) : Flow<ApiResponse<Nothing>>

    suspend fun sendEmailTokenConfirmation(email : String) : Flow<ApiResponse<Nothing>>

    suspend fun confirmEmail(token : String) : Flow<ApiResponse<Nothing>>

    suspend fun refreshToken() : ResultWrapper<TokenData, FailedRefreshTokenError>

    suspend fun  logout() : ApiResponse<Nothing>
}