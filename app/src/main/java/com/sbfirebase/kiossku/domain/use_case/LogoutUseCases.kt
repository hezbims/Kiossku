package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.domain.apiresponse.LogoutApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import javax.inject.Inject

class LogoutUseCases @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke() =
        try{
            if (authRepository.logout().isSuccessful)
                LogoutApiResponse.Success
            else
                LogoutApiResponse.Failed
        }catch (e : Exception){
            LogoutApiResponse.Failed
        }
}