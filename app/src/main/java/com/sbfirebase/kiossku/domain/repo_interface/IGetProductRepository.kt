package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import kotlinx.coroutines.flow.Flow

interface IGetProductRepository {
    suspend fun getAllProduct(token : String) : AuthorizedApiResponse<List<KiosDataDto?>>

    suspend fun getProductById(id : Int , token : String) : Flow<AuthorizedApiResponse<KiosDataDto>>
}