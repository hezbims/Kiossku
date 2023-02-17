package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.model.UserData
import com.sbfirebase.kiossku.domain.repo_interface.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserUseCase @Inject constructor(
    private val authManager: AuthManager,
    private val userRepository : IUserRepository
) {
    suspend operator fun invoke() : ApiResponse<UserData> {
        val userId = authManager.getUserId()
        assert(userId != -1)

        return when (val tokenResponse = authManager.getToken()) {
            is ApiResponse.Success ->
                userRepository.getUser(userId = userId, token = tokenResponse.data!!)
            is ApiResponse.Failure ->
                ApiResponse.Failure(errorCode = tokenResponse.errorCode)
            else -> throw IllegalArgumentException("GetUserUseCase token mereturn loading...")
        }
    }
}