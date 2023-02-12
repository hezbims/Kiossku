package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.data.model.user.UpdateUserDto
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IUserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository : IUserRepository,
    private val authManager : AuthManager
) {
    suspend operator fun invoke(
        userNewData : UpdateUserDto
    ) : AuthorizedApiResponse<Nothing>{
        return when (val tokenResponse = authManager.getToken()) {
            is AuthorizedApiResponse.Success ->
                userRepository.updateUser(
                    userId = authManager.getUserId(),
                    userNewData = userNewData,
                    token = tokenResponse.data!!
                )
            is AuthorizedApiResponse.Failure ->
                AuthorizedApiResponse.Failure(
                    errorCode = tokenResponse.errorCode,
                    errorMessage = tokenResponse.errorMessage,
                )
            else -> throw IllegalArgumentException("GetToken Mereturn Loading")
        }
    }
}