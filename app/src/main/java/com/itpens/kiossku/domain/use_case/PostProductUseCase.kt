package com.itpens.kiossku.domain.use_case

import com.itpens.kiossku.data.model.postproduct.PostKiosData
import com.itpens.kiossku.domain.AuthManager
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.repo_interface.IPostProductRepository
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