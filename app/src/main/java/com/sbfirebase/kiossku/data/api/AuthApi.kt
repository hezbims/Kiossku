package com.sbfirebase.kiossku.data.api

import com.sbfirebase.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.sbfirebase.kiossku.data.model.login.SuccessfulLoginResponse
import com.sbfirebase.kiossku.data.model.logout.LogoutResponse
import com.sbfirebase.kiossku.data.model.register.RegisterErrorResponse
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import retrofit2.Call
import retrofit2.http.*

interface AuthApi {
    @POST("register")
    fun register(
        @Body post : RegisterPost
    ) : Call<List<RegisterErrorResponse>>

    @DELETE("logout")
    fun logout() : Call<LogoutResponse>

    @GET("token/")
    fun refreshToken() : Call<SuccessfulRefreshTokenResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<SuccessfulLoginResponse>
}