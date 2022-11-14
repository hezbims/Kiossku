package com.sbfirebase.kiossku.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kioss(
    val id : Long = -1,
    val judul : String,
    val alamat : String,
    val harga : String,
    val gambar : String,
    val tipeProperti : String,
    val fixAtauNego : String,
    val luasBangunan : String,
    val luasLahan : String,
    val kapasitasListrik : String,
    val email : String,
    val phone : String
) : Parcelable
