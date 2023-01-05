package com.sbfirebase.kiossku.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface DaerahApi{
    @GET("provinsi.json")
    fun getProvinsi() : List<Daerah>

    @GET("kabupaten/{id_provinsi}.json")
    fun getKabupaten(@Path("id_provinsi") idProvinsi : String) : List<Daerah>

    @GET("kecamatan/{id_kabupaten}.json")
    fun getKecamatan(@Path("id_kabupaten") idKabupaten : String) : List<Daerah>

    @GET("kelurahan/{id_kecamatan}.json")
    fun getKelurahan(@Path("id_kecamatan") idKecamatan : String) : List<Daerah>
}


data class Daerah(
    val id : String,
    val nama : String
){
    override fun toString() = nama
}