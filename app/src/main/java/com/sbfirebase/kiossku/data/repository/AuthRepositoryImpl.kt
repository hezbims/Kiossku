package com.sbfirebase.kiossku.data.repository

import android.util.Log
import com.sbfirebase.kiossku.constant.ApiMessage
import com.sbfirebase.kiossku.data.api.AuthApi
import com.sbfirebase.kiossku.data.model.login.LoginDto
import com.sbfirebase.kiossku.data.model.logout.LogoutResponse
import com.sbfirebase.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.mapper.Mapper
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiClient : AuthApi,
    private val errorBodyToMessage : Mapper<String , String>
) : IAuthRepository {
    override suspend fun login(
        email : String,
        password : String
    ): Flow<AuthorizedApiResponse<LoginDto>> =
        flow {
            emit(AuthorizedApiResponse.Loading())
            try{
                val response = authApiClient.login(
                    email = email,
                    password = password
                )

                if (response.isSuccessful)
                    emit(AuthorizedApiResponse.Success(data = response.body()!!))
                else
                    emit(AuthorizedApiResponse.Failure(errorMessage = "Password dan email tidak cocok!"))
            }catch (e : Exception){
                emit(AuthorizedApiResponse.Failure(errorMessage = "Login gagal, cek internet anda!"))
            }
        }

    override suspend fun register(
        registerBody : RegisterPost
    ): Flow<AuthorizedApiResponse<Nothing>> =
        flow {
            emit(AuthorizedApiResponse.Loading())
            try{
                val response = authApiClient.register(registerBody)

                if (response.isSuccessful)
                    emit(AuthorizedApiResponse.Success())
                else {
                    val message = errorBodyToMessage.from(
                        data = response.errorBody()!!.string()
                    )
                    emit(
                        AuthorizedApiResponse.Failure(
                            errorMessage = message
                        )
                    )

                    Log.e("qqq" , "Register unsuccessful : $message")
                }
            }
            catch (e : Exception){
                Log.e("qqq" , "Register error : ${e.localizedMessage}")
                emit(
                    AuthorizedApiResponse.Failure(
                        errorMessage = ApiMessage.INTERNET_FAILED
                    )
                )
            }
        }

    override suspend fun sendEmailTokenConfirmation(email: String) : Flow<AuthorizedApiResponse<Nothing>> =
        flow{
            emit(AuthorizedApiResponse.Loading())
            try{
                val response = authApiClient.sendEmailTokenConfirmation(email)
                if (response.isSuccessful)
                    emit(AuthorizedApiResponse.Success())
                else{
                    Log.e("qqqSendEmailTokenFailed" , "${response.errorBody()?.string()}")
                    emit(AuthorizedApiResponse.Failure())
                }
            }catch (e : Exception){
                Log.e("qqqSendEmailError" , e.localizedMessage ?: "Unknown error")
                emit(AuthorizedApiResponse.Failure())
            }
        }

    override suspend fun confirmEmail(token: String): Flow<AuthorizedApiResponse<Nothing>> =
        flow {
            emit(AuthorizedApiResponse.Loading())
            try{
                val response = authApiClient.confirmEmail(token)
                if (response.isSuccessful)
                    emit(AuthorizedApiResponse.Success())
                else{
                    Log.e(
                        "qqqConfirmEmailFailed" ,
                        response.errorBody()?.string() ?: "Unknown error"
                    )
                    emit(AuthorizedApiResponse.Failure(errorCode = response.code()))
                }
            }catch (e : Exception){
                Log.e("qqqConfirmEmailError" , e.localizedMessage ?: "Unknown error")
                emit(AuthorizedApiResponse.Failure())
            }
        }

    override suspend fun refreshToken(): Response<SuccessfulRefreshTokenResponse> =
        authApiClient.refreshToken().execute()

    override suspend fun logout(): Response<LogoutResponse> =
        authApiClient.logout().execute()


}