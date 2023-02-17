package com.sbfirebase.kiossku.domain.use_case

import android.util.Log
import com.sbfirebase.kiossku.domain.TokenManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import javax.inject.Inject

class RefreshTokenUseCases @Inject constructor(
    private val authRepository: IAuthRepository,
    private val tokenManager : TokenManager
){
    suspend operator fun invoke() : ApiResponse<String> =
        try{
            val response = authRepository.refreshToken()
            if (response.isSuccessful) {
                val token = response.body()?.data?.token!!
                tokenManager.setTokenSync(token = token)
                ApiResponse.Success(data = token)
            }
            else {
                Log.e("qqqRefTokenRepo" , response.errorBody()!!.string())
                ApiResponse.Failure(errorCode = response.code())
            }
        }catch (e : Exception){
            ApiResponse.Failure()
        }
}