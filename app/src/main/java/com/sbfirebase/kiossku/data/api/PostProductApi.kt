package com.sbfirebase.kiossku.data.api

import com.sbfirebase.kiossku.data.model.postproduct.PostProductDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface PostProductApi {
    @POST("add")
    suspend fun postProduct(
        @Body body : MultipartBody,
        @Header("Authorization") token : String
    ) : Response<PostProductDto>
}