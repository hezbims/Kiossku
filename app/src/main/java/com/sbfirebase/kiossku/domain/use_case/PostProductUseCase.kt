package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IPostProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostProductUseCase @Inject constructor(
    private val authManager: AuthManager,
    private val postProductRepository : IPostProductRepository
) {
    suspend operator fun invoke(data : PostKiosData) : AuthorizedApiResponse<Nothing>{
        val tokenResponse = authManager.getToken()
        return when (tokenResponse){
            is AuthorizedApiResponse.Success ->
               postProductRepository.submitProduct(postData = data , token = tokenResponse.data!!)
            is AuthorizedApiResponse.Failure ->
                AuthorizedApiResponse.Failure()
            else -> throw IllegalArgumentException("Get token mengembalikan loading...")
        }

    }
}