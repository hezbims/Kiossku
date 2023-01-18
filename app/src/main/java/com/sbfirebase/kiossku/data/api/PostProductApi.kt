package com.sbfirebase.kiossku.data.api

import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.data.model.postproduct.SuccessfulPostProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PostProductApi {
    @POST("add")
    suspend fun postProduct(
        @Body data : PostKiosData,
        @Header("Authorization") token : String
    ) : Response<SuccessfulPostProductResponse>
}