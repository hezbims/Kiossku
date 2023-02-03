package com.sbfirebase.kiossku.data.repository

import com.sbfirebase.kiossku.data.api.GetProductApi
import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IGetProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductRepositoryImpl @Inject constructor(
    private val getProductApiClient : GetProductApi
) : IGetProductRepository{
    override suspend fun getAllProduct(token : String) : AuthorizedApiResponse<List<KiosDataDto?>>{
        return try{
            val response = getProductApiClient.getAllProduct(token = "Bearer $token")
            if (response.isSuccessful)
               AuthorizedApiResponse.Success(data = response.body()?.data!!)
            else
                AuthorizedApiResponse.Failure(errorCode = response.code())
        }catch (e : Exception){
            AuthorizedApiResponse.Failure()
        }
    }

    override suspend fun getProductById(id: Int , token : String):
            Flow<AuthorizedApiResponse<KiosDataDto>> =
        flow {
            emit(AuthorizedApiResponse.Loading())
            try{
                val product = getProductApiClient.getProductById(id , token)
                emit(AuthorizedApiResponse.Success(product))
            }catch (e : Exception){
                emit(AuthorizedApiResponse.Failure(null))
            }
        }
}