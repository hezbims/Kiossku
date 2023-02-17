package com.sbfirebase.kiossku.domain.repo_interface

import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse

interface IPostProductRepository {
    suspend fun submitProduct(
        postData : PostKiosData,
        token : String
    ) : ApiResponse<Nothing>

}