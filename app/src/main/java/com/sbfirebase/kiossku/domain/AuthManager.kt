package com.sbfirebase.kiossku.domain

import android.util.Log
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.apiresponse.LogoutApiResponse
import com.sbfirebase.kiossku.domain.apiresponse.RefreshTokenApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import com.sbfirebase.kiossku.domain.use_case.LogoutUseCases
import com.sbfirebase.kiossku.domain.use_case.RefreshTokenUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshTokenUseCases: RefreshTokenUseCases,
    private val authRepository : IAuthRepository,
    private val logoutUseCases: LogoutUseCases
) {
    fun isLoggedIn() : Boolean = tokenManager.getToken() != null

    suspend fun getToken() : RefreshTokenApiResponse {
        return try {
            val result = if (!tokenManager.isTokenExpired) {
                Log.e("qqq" , "Masuk ke sini")
                RefreshTokenApiResponse.Success(tokenManager.getToken()!!)
            }
            else
                refreshToken()
            if (result is RefreshTokenApiResponse.Success)
                Log.e("qqqAuthGetToken" , "Token : ${result.token}")
            return result
        }catch (e : Exception){
            Log.e("qqqAuthGetToken" , e.localizedMessage?.toString() ?: "Unknown Error")
            RefreshTokenApiResponse.InternetFail
        }
    }


    suspend fun logOut() : LogoutApiResponse =
        logoutUseCases().apply {
            if (this is LogoutApiResponse.Success)
                tokenManager.setTokenSync(token = null)
        }

    suspend fun login(email : String , password : String) : Flow<AuthorizedApiResponse<String>> =
        authRepository.login(
            email = email,
            password = password
        ).onEach { response ->
            if (response is AuthorizedApiResponse.Success)
                tokenManager.setTokenSync(response.data!!)
        }

    suspend fun register(registerBody : RegisterPost) : Flow<AuthorizedApiResponse<Nothing>> =
        authRepository.register(registerBody = registerBody)

    private suspend fun refreshToken() : RefreshTokenApiResponse {
        return refreshTokenUseCases().apply {
            if (this is RefreshTokenApiResponse.Success)
                tokenManager.setTokenSync(token = token)
        }
    }
}
