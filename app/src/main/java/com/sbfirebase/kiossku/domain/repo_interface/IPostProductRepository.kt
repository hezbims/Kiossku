package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.data.model.postproduct.SuccessfulPostProductResponse
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import kotlinx.coroutines.flow.Flow

interface IPostProductRepository {
    suspend fun submitProduct(
        postData : PostKiosData,
        token : String
    ) : Flow<AuthorizedApiResponse<SuccessfulPostProductResponse>>

}