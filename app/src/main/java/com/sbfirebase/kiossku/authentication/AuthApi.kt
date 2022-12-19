package com.sbfirebase.kiossku.authentication

import com.sbfirebase.kiossku.authentication.logout.LogoutResponse
import com.sbfirebase.kiossku.authentication.register.RegisterPost
import com.sbfirebase.kiossku.authentication.register.RegisterResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    fun register(
        @Body post : RegisterPost
    ) : Call<List<RegisterResponse>>

    @DELETE("logout")
    fun logout() : Call<LogoutResponse>

    @GET("token/")
    fun refreshToken() : Call<RefreshTokenResponse>
}

val authApiClient = Retrofit.Builder()
    .baseUrl("https://kiossku.com/be-api/v1/auth/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create<AuthApi>()