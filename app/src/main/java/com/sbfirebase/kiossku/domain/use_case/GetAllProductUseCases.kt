package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IGetProductRepository
import javax.inject.Inject

class GetAllProductUseCases @Inject constructor(
    private val getProductRepository : IGetProductRepository,
    private val authManager: AuthManager
) {
    suspend operator fun invoke() : AuthorizedApiResponse<List<KiosDataDto?>> {
        val getTokenResponse = authManager.getToken()

        return when (getTokenResponse) {
            is AuthorizedApiResponse.Failure ->
                AuthorizedApiResponse.Failure(errorCode = getTokenResponse.errorCode)
            is AuthorizedApiResponse.Success ->
                getProductRepository.getAllProduct(token = getTokenResponse.data!!)
            is AuthorizedApiResponse.Loading ->
                throw IllegalArgumentException("Get Token mereturn Loading")
        }

    }
}