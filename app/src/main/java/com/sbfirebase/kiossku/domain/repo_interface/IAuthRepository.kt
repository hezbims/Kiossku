package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.login.LoginDto
import com.sbfirebase.kiossku.data.model.logout.LogoutResponse
import com.sbfirebase.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IAuthRepository {
    suspend fun login(email : String , password : String) : Flow<AuthorizedApiResponse<LoginDto>>

    suspend fun register(
        registerBody: RegisterPost
    ) : Flow<AuthorizedApiResponse<Nothing>>

    suspend fun sendEmailTokenConfirmation(email : String) : Flow<AuthorizedApiResponse<Nothing>>

    suspend fun confirmEmail(token : String) : Flow<AuthorizedApiResponse<Nothing>>

    suspend fun refreshToken() : Response<SuccessfulRefreshTokenResponse>

    suspend fun  logout() : Response<LogoutResponse>
}