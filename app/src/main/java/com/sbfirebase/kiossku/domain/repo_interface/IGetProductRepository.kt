package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.data.model.getproduct.SuccessfulGetKiosResponse
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IGetProductRepository {
    suspend fun getAllProduct(token : String) : Response<SuccessfulGetKiosResponse>

    suspend fun getProductById(id : Int , token : String) : Flow<AuthorizedApiResponse<KiosDataDto>>
}