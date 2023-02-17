package com.sbfirebase.kiossku.domain

import android.util.Log
import com.sbfirebase.kiossku.data.model.login.LoginDto
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.apiresponse.LogoutApiResponse
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
    fun getUserId() : Int = tokenManager.getUserId()

    fun isLoggedIn() : Boolean = tokenManager.getToken() != null

    suspend fun getToken() : ApiResponse<String> {
        return try {
            val savedToken = tokenManager.getToken()
            if (savedToken != null)
                ApiResponse.Success(data = savedToken)
            else
                refreshTokenUseCases()
        } catch (e: Exception) {
            Log.e("qqqAuthGetToken", e.localizedMessage?.toString() ?: "Unknown Error")
            ApiResponse.Failure()
        }
    }


    suspend fun logOut() : LogoutApiResponse =
        logoutUseCases().apply {
            if (this is LogoutApiResponse.Success)
                tokenManager.setTokenSync(token = null)
        }

    suspend fun login(email : String , password : String) : Flow<ApiResponse<LoginDto>> =
        authRepository.login(
            email = email,
            password = password
        ).onEach { response ->
            if (response is ApiResponse.Success) {
                tokenManager.setTokenSync(
                    token = response.data!!.loginData.token,
                )
                tokenManager.setUserId(response.data.loginData.id)
            }
        }

    suspend fun register(registerBody : RegisterPost) : Flow<ApiResponse<Nothing>> =
        authRepository.register(registerBody = registerBody)
}
