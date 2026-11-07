package com.itpens.kiossku.data.repository

import com.itpens.kiossku.data.api.GetProductApi
import com.itpens.kiossku.data.model.getproduct.KiosDataDto
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import com.itpens.kiossku.domain.repo_interface.IGetProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductRepositoryImpl @Inject constructor(
    private val getProductApiClient : GetProductApi
) : IGetProductRepository{
    override suspend fun getAllProduct(token : String) : ApiResponse<List<KiosDataDto?>>{
        return try{
            val response = getProductApiClient.getAllProduct(token = "Bearer $token")
            if (response.isSuccessful)
               ApiResponse.Success(data = response.body()?.data!!)
            else
                ApiResponse.Failure(errorCode = response.code())
        }catch (e : Exception){
            ApiResponse.Failure()
        }
    }

    override suspend fun getProductById(id: Int , token : String):
            Flow<ApiResponse<KiosDataDto>> =
        flow {
            emit(ApiResponse.Loading())
            try{
                val product = getProductApiClient.getProductById(id , token)
                emit(ApiResponse.Success(product))
            }catch (e : Exception){
                emit(ApiResponse.Failure(null))
            }
        }
}