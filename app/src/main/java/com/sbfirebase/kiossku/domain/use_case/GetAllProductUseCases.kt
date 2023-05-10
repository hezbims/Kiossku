package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IGetProductRepository
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class GetAllProductUseCases @Inject constructor(
    private val getProductRepository : IGetProductRepository,
    private val authManager: AuthManager
) : IUseCases<ApiResponse<@JvmSuppressWildcards List<KiosDataDto?>>> {
    override suspend operator fun invoke() : ApiResponse<List<KiosDataDto?>> {
        val getTokenResponse = authManager.getToken()

        return when (getTokenResponse) {
            is ApiResponse.Failure ->
                ApiResponse.Failure(errorCode = getTokenResponse.errorCode)
            is ApiResponse.Success ->
                getProductRepository.getAllProduct(token = getTokenResponse.data!!)
            is ApiResponse.Loading ->
                throw IllegalArgumentException("Get Token mereturn Loading")
        }

    }
}