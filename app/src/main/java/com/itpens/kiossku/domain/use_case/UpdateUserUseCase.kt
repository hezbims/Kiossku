package com.itpens.kiossku.domain.use_case

import com.itpens.kiossku.data.model.user.UpdateUserDto
import com.itpens.kiossku.domain.AuthManager
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.repo_interface.IUserRepository
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