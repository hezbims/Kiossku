package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IPostProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostProductUseCase @Inject constructor(
    private val authManager: AuthManager,
    private val postProductRepository : IPostProductRepository
) {
    suspend operator fun invoke(data : PostKiosData) : ApiResponse<Nothing>{
        val tokenResponse = authManager.getToken()
        return when (tokenResponse){
            is ApiResponse.Success ->
               postProductRepository.submitProduct(postData = data , token = tokenResponse.data!!)
            is ApiResponse.Failure ->
                ApiResponse.Failure()
            else -> throw IllegalArgumentException("Get token mengembalikan loading...")
        }

    }
}