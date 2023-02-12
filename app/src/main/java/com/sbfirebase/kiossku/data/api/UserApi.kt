package com.sbfirebase.kiossku.data.api

import com.sbfirebase.kiossku.data.model.user.GetUserDto
import com.sbfirebase.kiossku.data.model.user.UpdateUserDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("user/{user_id}")
    suspend fun getUser(@Path("user_id") userId : Int , @Header("Authorization") token : String) : Response<GetUserDto>

    @PUT("update/{user_id}")
    suspend fun updateUser(
        @Path("user_id") userId : Int ,
        @Body body : UpdateUserDto ,
        @Header("Authorization") token : String
    ) : Response<ResponseBody>
}