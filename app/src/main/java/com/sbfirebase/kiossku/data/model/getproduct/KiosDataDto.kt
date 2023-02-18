package com.sbfirebase.kiossku.data.model.getproduct

import com.google.gson.annotations.SerializedName
import com.sbfirebase.kiossku.domain.model.KiosData

data class KiosDataDto(

    @field:SerializedName("kapasitas_listrik")
    val kapasitasListrik: Int? = null,

    @field:SerializedName("luas_lahan")
    val luasLahan: Int? = null,

    @field:SerializedName("images")
    val images: List<KiosImageDto>? = null,

    // sewa atau jual
    @field:SerializedName("sistem")
    val sistem: String? = null,


    // tahunan atau bulanan
    @field:SerializedName("tipe_harga")
    val tipeHarga: String? = null,

    @field:SerializedName("luas_bangunan")
    val luasBangunan: Int? = null,

    @field:SerializedName("fasilitas")
    val fasilitas: String? = null,

    @field:SerializedName("alamat_lengkap")
    val alamatLengkap: String? = null,

    @field:SerializedName("tingkat")
    val tingkat: Int? = null,

    @field:SerializedName("panjang")
    val panjang: Int? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("lokasi")
    val lokasi: String? = null,

    @field:SerializedName("product_id")
    val productId: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    // jenis properti
    @field:SerializedName("jenis")
    val jenis: String? = null,

    // fix atau nego
    @field:SerializedName("tipe_pembayaran")
    val tipePembayaran: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("deskripsi")
    val deskripsi: String? = null,

    @field:SerializedName("lebar")
    val lebar: Int? = null,

    @field:SerializedName("status")
    val status: Int? = null
){
    suspend fun mapToKiosData() : KiosData{
        val listOfImageAsString = images?.joinToString(separator = ",") {
            it.image!!
        } ?: ""

        return KiosData(
            alamat = alamatLengkap!!,
            deskripsi = deskripsi,
            fasilitas = fasilitas,
            harga = harga!!,
            images = listOfImageAsString,
            jenisProperti = jenis!!,
            judulPromosi = name!!,
            kapasitasListrik = kapasitasListrik!!,
            lebar = lebar!!,
            lokasi = lokasi!!,
            luasBangunan = luasBangunan!!,
            luasLahan = luasLahan!!,
            panjang = panjang!!,
            productId = productId!!,
            sistemSewaJual = sistem!!,
            status = status!!,
            negoFix = tipePembayaran!!,
            tingkat = tingkat!!,
            tahunanBulanan = tipeHarga!!,
            userId = userId!!
        )
    }
}

data class KiosImageDto(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("product_id")
    val productId: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null
)