package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.domain.apiresponse.LoginApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
import javax.inject.Inject

class LoginUseCases @Inject constructor(
    private val authRepository: IAuthRepository
){
    // diasumsikan email dan password tidak kosong
    suspend operator fun invoke(email : String , password : String) : LoginApiResponse{
        return try {
            val response = authRepository.login(
                email = email,
                password = password
            )
            LoginApiResponse.Success(response.body()?.loginData!!)
        }catch (e : Exception){
            LoginApiResponse.Failed
        }
    }
}