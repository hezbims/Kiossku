package com.sbfirebase.kiossku.domain.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class KiosData(
    val kapasitasListrik : Int,
    val luasLahan : Int,
    val images : String,
    val sistemSewaJual : String, // sewa atau jual
    val tahunanBulanan : String,
    val luasBangunan : Int,
    val fasilitas : String?,
    val alamat : String,
    val tingkat : Int,
    val panjang : Int,
    val harga : Int,
    val userId : Int,
    val lokasi : String,
    val productId : Int,
    val judulPromosi : String,
    val jenisProperti : String,
    val negoFix : String,
    val deskripsi : String?,
    val lebar : Int,
    val status : Int
) : Parcelable{
    fun toJsonString() : String{
        return Gson().toJson(KiosData::class.java)
    }
}

class KiosDataType : NavType<KiosData>(isNullableAllowed = false){
    override fun get(bundle: Bundle, key: String): KiosData? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            return bundle.getParcelable(key)
        return bundle.getParcelable(key , KiosData::class.java)
    }

    override fun parseValue(value: String): KiosData {
        return Gson().fromJson(value , KiosData::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: KiosData) {
        bundle.putParcelable(key , value)
    }
}