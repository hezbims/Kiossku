package com.sbfirebase.kiossku.domain.use_case

import android.util.Log
import com.sbfirebase.kiossku.domain.TokenManager
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import javax.inject.Inject

class RefreshTokenUseCases @Inject constructor(
    private val authRepository: IAuthRepository,
    private val tokenManager : TokenManager
){
    suspend operator fun invoke() : AuthorizedApiResponse<String> =
        try{
            val response = authRepository.refreshToken()
            if (response.isSuccessful) {
                val token = response.body()?.data?.token!!
                tokenManager.setTokenSync(token = token)
                AuthorizedApiResponse.Success(data = token)
            }
            else {
                Log.e("qqqRefTokenRepo" , response.errorBody()!!.string())
                AuthorizedApiResponse.Failure(errorCode = response.code())
            }
        }catch (e : Exception){
            AuthorizedApiResponse.Failure()
        }
}