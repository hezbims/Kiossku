package com.sbfirebase.kiossku.home

import com.sbfirebase.kiossku.data.GetKiosResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Header

interface GetProductApi {
    @GET("all")
    fun getAllProduct(@Header("Authorization") token : String) : Call<GetKiosResponse>
}

val getProductApiClient = Retrofit.Builder()
    .baseUrl("https://kiossku.com/be-api/v1/product/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create<GetProductApi>()