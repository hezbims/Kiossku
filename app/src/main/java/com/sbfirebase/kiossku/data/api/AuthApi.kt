package com.sbfirebase.kiossku.data.api

import com.sbfirebase.kiossku.data.model.login.LoginDto
import com.sbfirebase.kiossku.data.model.logout.LogoutResponse
import com.sbfirebase.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("register")
    suspend fun register(
        @Body post : RegisterPost
    ) : Response<ResponseBody>

    @DELETE("logout")
    fun logout() : Call<LogoutResponse>

    @GET("token/")
    fun refreshToken() : Call<SuccessfulRefreshTokenResponse>

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