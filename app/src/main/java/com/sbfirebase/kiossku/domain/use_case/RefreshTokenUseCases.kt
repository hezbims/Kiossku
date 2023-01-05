package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.domain.apiresponse.RefreshTokenApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import javax.inject.Inject

class RefreshTokenUseCases @Inject constructor(
    private val authRepository: IAuthRepository
){
    suspend operator fun invoke() : RefreshTokenApiResponse =
        try{
            val response = authRepository.refreshToken()
            if (response.isSuccessful)
                RefreshTokenApiResponse
                    .Success(response.body()?.data?.token!!)
            else
                RefreshTokenApiResponse.Unauthorized
        }catch (e : Exception){
            RefreshTokenApiResponse.InternetFail
        }
}