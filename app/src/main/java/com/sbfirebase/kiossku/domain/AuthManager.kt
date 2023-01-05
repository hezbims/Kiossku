package com.sbfirebase.kiossku.domain

import com.sbfirebase.kiossku.domain.apiresponse.LoginApiResponse
import com.sbfirebase.kiossku.domain.apiresponse.LogoutApiResponse
import com.sbfirebase.kiossku.domain.apiresponse.RefreshTokenApiResponse
import com.sbfirebase.kiossku.domain.use_case.LoginUseCases
import com.sbfirebase.kiossku.domain.use_case.LogoutUseCases
import com.sbfirebase.kiossku.domain.use_case.RefreshTokenUseCases
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshTokenUseCases: RefreshTokenUseCases,
    private val loginUseCases: LoginUseCases,
    private val logoutUseCases: LogoutUseCases
) {
    fun isLoggedIn() : Boolean = tokenManager.getToken() != null

    suspend fun getToken() : RefreshTokenApiResponse =
        if (!tokenManager.isTokenExpired)
            RefreshTokenApiResponse.Success(tokenManager.getToken()!!)
        else
            refreshToken()


    suspend fun logOut() : LogoutApiResponse =
        logoutUseCases().apply {
            if (this is LogoutApiResponse.Success)
                tokenManager.setTokenSync(token = null)
        }

    suspend fun login(email : String , password : String) : LoginApiResponse{
        return loginUseCases(email = email , password = password).apply {
            if (this is LoginApiResponse.Success)
                tokenManager.setTokenSync(token = loginData.token)
        }
    }

    private suspend fun refreshToken() : RefreshTokenApiResponse {
        return refreshTokenUseCases().apply {
            if (this is RefreshTokenApiResponse.Success)
                tokenManager.setTokenSync(token = token)
        }
    }
}
