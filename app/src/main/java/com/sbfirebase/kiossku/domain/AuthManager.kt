package com.sbfirebase.kiossku.domain

import android.util.Log
import com.sbfirebase.kiossku.data.model.login.LoginDto
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import com.sbfirebase.kiossku.domain.use_case.RefreshTokenUseCases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshTokenUseCases: RefreshTokenUseCases,
    private val authRepository : IAuthRepository
) : IAuthManager {
    override fun getUserId() : Int = tokenManager.getUserId()

    override fun isLoggedIn() : Boolean = tokenManager.getToken() != null

    override suspend fun getToken() : ApiResponse<String> {
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


    override suspend fun logOut() : ApiResponse<Nothing> =
        authRepository.logout().apply {
            if (this is ApiResponse.Success)
                tokenManager.setTokenSync(null)
        }

    override suspend fun login(email : String, password : String) : ApiResponse<LoginDto> =
        authRepository.login(
            email = email,
            password = password
        ).apply {
            if (this is ApiResponse.Success) {
                tokenManager.setTokenSync(
                    token = data!!.loginData.token,
                )
                tokenManager.setUserId(data.loginData.id)
            }
        }

    override suspend fun register(registerBody : RegisterPost) : Flow<ApiResponse<Nothing>> =
        authRepository.register(registerBody = registerBody)
}
