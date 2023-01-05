package com.sbfirebase.kiossku.domain.use_case

import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.GetProductApiResponse
import com.sbfirebase.kiossku.domain.apiresponse.RefreshTokenApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IGetProductRepository
import javax.inject.Inject

class GetAllProductUseCases @Inject constructor(
    private val getProductRepository : IGetProductRepository,
    private val authManager: AuthManager
) {
    suspend operator fun invoke() : GetProductApiResponse {
        val getTokenResponse = authManager.getToken()
        return when (getTokenResponse) {
            RefreshTokenApiResponse.InternetFail -> GetProductApiResponse.Failed
            RefreshTokenApiResponse.Unauthorized -> GetProductApiResponse.Unauthorized
            is RefreshTokenApiResponse.Success ->
                try {
                    val getAllProductResponse = getProductRepository.getAllProduct(
                        token = getTokenResponse.token
                    )
                    GetProductApiResponse.Success(
                        getAllProductResponse.body()?.data!!
                    )
                } catch (e: Exception) {
                    GetProductApiResponse.Failed
                }
        }
    }
}