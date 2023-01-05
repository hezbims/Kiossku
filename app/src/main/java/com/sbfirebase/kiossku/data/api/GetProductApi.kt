package com.sbfirebase.kiossku.data.api

import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.data.model.getproduct.SuccessfulGetKiosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetProductApi {
    @GET("all")
    fun getAllProduct(@Header("Authorization") token : String) : Call<SuccessfulGetKiosResponse>

    @GET("{product_id}")
    fun getProductById(
        @Path("product_id") productId : Int,
        @Header("Authorization") token : String
    ) : KiosDataDto
}
