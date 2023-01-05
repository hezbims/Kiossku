package com.sbfirebase.kiossku.data.repository

import com.sbfirebase.kiossku.data.api.AuthApi
import com.sbfirebase.kiossku.data.model.login.SuccessfulLoginResponse
import com.sbfirebase.kiossku.data.model.logout.LogoutResponse
import com.sbfirebase.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiClient : AuthApi
) : IAuthRepository {
    override suspend fun login(
        email : String,
        password : String
    ): Response<SuccessfulLoginResponse> =
        authApiClient.login(
            email = email,
            password = password
        ).execute()

    override suspend fun refreshToken(): Response<SuccessfulRefreshTokenResponse> =
        authApiClient.refreshToken().execute()

    override suspend fun logout(): Response<LogoutResponse> =
        authApiClient.logout().execute()
}