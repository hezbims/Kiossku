package com.sbfirebase.kiossku.data.model.postproduct

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class PostKiosData(
    @SerializedName("name")
    val judulPromosi : String,

    @SerializedName("jenis")
    val tipeProperti : String,

    @SerializedName("harga")
    val harga : Int,

    @SerializedName("tipe_harga")
    val waktuPembayaran : String,

    @SerializedName("tipe_pembayaran")
    val fixNego : String,

    @SerializedName("sistem")
    val sewaJual : String,

    @SerializedName("lokasi")
    val lokasi : String,

    @SerializedName("luas_lahan")
    val luasLahan : Int,

    @SerializedName("luas_bangunan")
    val luasBangunan : Int,

    @SerializedName("tingkat")
    val tingkat : Int,

    @SerializedName("kapasitas_listrik")
    val kapasitasListrik : Int,

    @SerializedName("alamat_lengkap")
    val alamat : String,

    @SerializedName("fasilitas")
    val fasilitas : String,

    @SerializedName("deskripsi")
    val deskripsi : String,

    @SerializedName("panjang")
    val panjang : Int,

    @SerializedName("lebar")
    val lebar : Int,

    @SerializedName("images")
    val images : List<Uri>
)
