package com.itpens.kiossku.domain.use_case

import com.itpens.kiossku.domain.AuthManager
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.model.UserData
import com.itpens.kiossku.domain.repo_interface.IUserRepository
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

        return userRepository.getUser(userId = userId)
    }
}