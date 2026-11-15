package com.itpens.kiossku.data.repository

import android.util.Log
import com.itpens.kiossku.constant.ApiMessage
import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.model.ApiError
import com.itpens.kiossku.data.model.auth.FailedRefreshTokenError
import com.itpens.kiossku.data.model.auth.TokenData
import com.itpens.kiossku.data.model.login.LoginDto
import com.itpens.kiossku.data.model.register.RegisterPost
import com.itpens.kiossku.data.utils.safeApiCall
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.mapper.Mapper
import com.itpens.kiossku.domain.model.ResultWrapper
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.domain.repo_interface.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiClient : AuthApi,
    private val errorBodyToMessage : Mapper<String , String>,
    private val tokenManager : ITokenManager,
    private val sessionManager : SessionManager,
) : IAuthRepository {
    override suspend fun login(
        email : String,
        password : String
    ): ApiResponse<LoginDto> =
        try{
            val response = authApiClient.login(
                email = email,
                password = password
            )

            if (response.isSuccessful){
                response.body()!!.loginData.let{ loginData ->
                    // Persist token ke USER_PROFILE_KEY supaya terbaca oleh
                    // AuthHeaderInterceptor (cek expiry JWT) dan di-link
                    // dengan userId untuk pemakaian API lain.
                    tokenManager.setUserProfile(
                        TokenData(
                            accessToken = loginData.token,
                            refreshToken = loginData.token
                        )
                    )
                    tokenManager.setUserId(loginData.id)
                }

                sessionManager.markSessionValid()

                ApiResponse.Success(data = response.body()!!)
            }
            else
                ApiResponse.Failure(errorMessage = "Password dan email tidak cocok!")
        }catch (e : Exception){
            Log.e("qqqLoginError" , e.localizedMessage ?: "Unknown Error")
            ApiResponse.Failure(errorMessage = "Login gagal, cek internet anda!")
        }

    override suspend fun register(
        registerBody : RegisterPost
    ): Flow<ApiResponse<Nothing>> =
        flow {
            emit(ApiResponse.Loading())
            try{
                val response = authApiClient.register(registerBody)

                if (response.isSuccessful)
                    emit(ApiResponse.Success())
                else {
                    val message = errorBodyToMessage.from(
                        data = response.errorBody()!!.string()
                    )
                    emit(
                        ApiResponse.Failure(
                            errorMessage = message
                        )
                    )

                    Log.e("qqq" , "Register unsuccessful : $message")
                }
            }
            catch (e : Exception){
                Log.e("qqq" , "Register error : ${e.localizedMessage}")
                emit(
                    ApiResponse.Failure(
                        errorMessage = ApiMessage.INTERNET_FAILED
                    )
                )
            }
        }

    override suspend fun sendEmailTokenConfirmation(email: String) : Flow<ApiResponse<Nothing>> =
        flow{
            emit(ApiResponse.Loading())
            try{
                val response = authApiClient.sendEmailTokenConfirmation(email)
                if (response.isSuccessful)
                    emit(ApiResponse.Success())
                else{
                    Log.e("qqqSendEmailTokenFailed" , "${response.errorBody()?.string()}")
                    emit(ApiResponse.Failure())
                }
            }catch (e : Exception){
                Log.e("qqqSendEmailError" , e.localizedMessage ?: "Unknown error")
                emit(ApiResponse.Failure())
            }
        }

    override suspend fun confirmEmail(token: String): Flow<ApiResponse<Nothing>> =
        flow {
            emit(ApiResponse.Loading())
            try{
                val response = authApiClient.confirmEmail(token)
                if (response.isSuccessful)
                    emit(ApiResponse.Success())
                else{
                    Log.e(
                        "qqqConfirmEmailFailed" ,
                        response.errorBody()?.string() ?: "Unknown error"
                    )
                    emit(ApiResponse.Failure(errorCode = response.code()))
                }
            }catch (e : Exception){
                Log.e("qqqConfirmEmailError" , e.localizedMessage ?: "Unknown error")
                emit(ApiResponse.Failure())
            }
        }

    override suspend fun refreshToken() : ResultWrapper<TokenData, FailedRefreshTokenError> {
        val refreshTokenResponse = safeApiCall { authApiClient.refreshToken() }

        if (refreshTokenResponse is ResultWrapper.Failure) {
            val error = refreshTokenResponse.error
            if (error is ApiError.Http && (
                error.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED ||
                error.statusCode == HttpURLConnection.HTTP_BAD_REQUEST
            )){
                return ResultWrapper.Failure(FailedRefreshTokenError.InvalidRefreshToken)
            }

            return ResultWrapper.Failure(FailedRefreshTokenError.Other(error.toDomain()))
        }

        val tokenResponse = (refreshTokenResponse as ResultWrapper.Success).data.data
        val tokenData = TokenData(
            accessToken = tokenResponse.token,
            refreshToken = tokenResponse.refreshToken
        )

        tokenManager.setUserProfile(tokenData)

        return ResultWrapper.Success(tokenData)
    }

    override suspend fun logout() : ApiResponse<Nothing> =
        try{
            val response = authApiClient.logout()
            if (response.isSuccessful)
                ApiResponse.Success()
            else
                ApiResponse.Failure(errorCode = response.code())
        }catch (e : Exception){
            Log.e("qqqLogoutError" , e.localizedMessage ?: "Unknown Error")
            ApiResponse.Failure()
        }



}