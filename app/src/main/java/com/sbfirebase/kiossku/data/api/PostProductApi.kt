package com.sbfirebase.kiossku.data.api

import com.sbfirebase.kiossku.data.model.postproduct.PostProductDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface PostProductApi {
    @Multipart
    @POST("add")
    suspend fun postProduct(
        @Part("name") judulPromosi : RequestBody,
        @Part("jenis") tipeProperti : RequestBody,
        @Part("harga") harga : RequestBody,
        @Part("tipe_harga") waktuPembayaran : RequestBody,
        @Part("tipe_pembayaran") fixNego : RequestBody,
        @Part("sistem") sewaJual : RequestBody,
        @Part("lokasi") lokasi : RequestBody,
        @Part("luas_lahan") luasLahan : RequestBody,
        @Part("luas_bangunan") luasBangunan : RequestBody,
        @Part("tingkat") tingkat : RequestBody,
        @Part("kapasitas_listrik") kapasitasListrik : RequestBody,
        @Part("alamat_lengkap") alamat : RequestBody,
        @Part("fasilitas") fasilitas : RequestBody,
        @Part("deskripsi") deskripsi : RequestBody,
        @Part("panjang") panjang : RequestBody,
        @Part("lebar") lebar : RequestBody,
        @Part images : List<MultipartBody.Part>,
        @Header("Authorization") token : String
    ) : Response<PostProductDto>
}