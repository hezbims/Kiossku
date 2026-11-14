package com.itpens.kiossku.data.api

import com.itpens.kiossku.data.model.login.LoginDto
import com.itpens.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.itpens.kiossku.data.model.register.RegisterPost
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("register")
    suspend fun register(
        @Body post : RegisterPost
    ) : Response<ResponseBody>

    @DELETE("logout")
    suspend fun logout() : Response<ResponseBody>

    @GET("token")
    suspend fun refreshToken() : Response<SuccessfulRefreshTokenResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Response<LoginDto>

    @FormUrlEncoded
    @POST("email/verification")
    suspend fun sendEmailTokenConfirmation(@Field("email") email: String) : Response<ResponseBody>

    @FormUrlEncoded
    @POST("email/confirm")
    suspend fun confirmEmail(@Field("token") token : String) : Response<ResponseBody>
}