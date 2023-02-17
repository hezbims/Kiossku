package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.data.model.user.UpdateUserDto
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IUserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository : IUserRepository,
    private val authManager : AuthManager
) {
    suspend operator fun invoke(
        userNewData : UpdateUserDto
    ) : ApiResponse<Nothing>{
        return when (val tokenResponse = authManager.getToken()) {
            is ApiResponse.Success ->
                userRepository.updateUser(
                    userId = authManager.getUserId(),
                    userNewData = userNewData,
                    token = tokenResponse.data!!
                )
            is ApiResponse.Failure ->
                ApiResponse.Failure(
                    errorCode = tokenResponse.errorCode,
                    errorMessage = tokenResponse.errorMessage,
                )
            else -> throw IllegalArgumentException("GetToken Mereturn Loading")
        }
    }
}