package com.sbfirebase.kiossku.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

const val baseURL = "https://kiossku.com/be-api/"

interface KiosskuAPI{

    @FormUrlEncoded
    @POST("v1/auth/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<LoginResponse>

    @GET("v1/product/all")
    fun getAllProducts(@Header("Authorization") token : String) : Call<GetProductResponse>
}

val api : KiosskuAPI = Retrofit.Builder()
    .baseUrl(baseURL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(KiosskuAPI::class.java)