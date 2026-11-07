package com.itpens.kiossku.domain.repo_interface

import com.itpens.kiossku.data.model.getproduct.KiosDataDto
import com.itpens.kiossku.domain.apiresponse.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IGetProductRepository {
    suspend fun getAllProduct(token : String) : ApiResponse<List<KiosDataDto?>>

    suspend fun getProductById(id : Int , token : String) : Flow<ApiResponse<KiosDataDto>>
}