package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.model.UserData
import com.sbfirebase.kiossku.domain.repo_interface.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserUseCase @Inject constructor(
    private val authManager: AuthManager,
    private val userRepository : IUserRepository
) {
    suspend operator fun invoke() : AuthorizedApiResponse<UserData> {
        val userId = authManager.getUserId()
        assert(userId != -1)

        return when (val tokenResponse = authManager.getToken()) {
            is AuthorizedApiResponse.Success ->
                userRepository.getUser(userId = userId, token = tokenResponse.data!!)
            is AuthorizedApiResponse.Failure ->
                AuthorizedApiResponse.Failure(errorCode = tokenResponse.errorCode)
            else -> throw IllegalArgumentException("GetUserUseCase token mereturn loading...")
        }
    }
}