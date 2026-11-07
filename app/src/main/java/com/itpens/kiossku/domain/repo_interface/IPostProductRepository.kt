package com.itpens.kiossku.domain.repo_interface

import com.itpens.kiossku.data.model.postproduct.PostKiosData
import com.itpens.kiossku.domain.apiresponse.ApiResponse

interface IPostProductRepository {
    suspend fun submitProduct(
        postData : PostKiosData,
        token : String
    ) : ApiResponse<Nothing>

}