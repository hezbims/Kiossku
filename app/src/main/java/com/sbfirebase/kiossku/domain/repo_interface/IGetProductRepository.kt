package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IGetProductRepository {
    suspend fun getAllProduct(token : String) : ApiResponse<List<KiosDataDto?>>

    suspend fun getProductById(id : Int , token : String) : Flow<ApiResponse<KiosDataDto>>
}