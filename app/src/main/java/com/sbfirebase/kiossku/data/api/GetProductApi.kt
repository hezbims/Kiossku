package com.sbfirebase.kiossku.data.api

import com.sbfirebase.kiossku.data.model.getproduct.GetAllProductDto
import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetProductApi {
    @GET("all")
    suspend fun getAllProduct(@Header("Authorization") token : String) : Response<GetAllProductDto>

    @GET("{product_id}")
    suspend fun getProductById(
        @Path("product_id") productId : Int,
        @Header("Authorization") token : String
    ) : KiosDataDto
}
