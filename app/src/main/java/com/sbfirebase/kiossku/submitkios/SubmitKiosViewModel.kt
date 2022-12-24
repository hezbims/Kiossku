package com.sbfirebase.kiossku.submitkios

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbfirebase.kiossku.submitkios.enumdata.*

class SubmitKiosViewModel(private val app : Application) : AndroidViewModel(app) {
    val sistemPembayaran = ElemenProperti<SistemPembayaran>(
        placeholder = "Pilih sistem pembayaran",
        errorMessage = "Sistem pembayaran"
    )

    val judulPromosi = ElemenProperti(
        placeholder = "Judul promosi",
        errorMessage = "Judul promosi",
        defValue = ""
    )
    val jenisProperti = ElemenProperti<JenisProperti>(
        placeholder = "Pilih jenis properti",
        errorMessage = "Jenis properti"
    )
    val waktuPembayaran = ElemenProperti(
        placeholder = "Pilih rentang pembayaran" ,
        defValue = WaktuPembayaran.TAHUNAN,
        errorMessage = "Rentang pembayaran"
    )
    val tipePenawaran = ElemenProperti<TipePenawaran>(
        placeholder = "Pilih tipe harga",
        errorMessage = "Tipe harga"
    )
    val harga = ElemenProperti<Long>(
        placeholder = "Harga",
        errorMessage = "Harga"
    )
    val luasLahan = ElemenProperti<Long>(
        placeholder = "Luas lahan",
        errorMessage = "Luas lahan"
    )
    val panjang = ElemenProperti<Long>(
        placeholder = "Panjang",
        errorMessage = "Panjang"
    )
    val lebar = ElemenProperti<Long>(
        placeholder = "Lebar",
        errorMessage = "Lebar"
    )
    val jumlahLantai = ElemenProperti<Long>(
        placeholder = "Jumlah lantai",
        errorMessage = "Jumlah lantai"
    )
    val kapasitasListrik = ElemenProperti<Long>(
        placeholder = "Kapasitas listrik",
        errorMessage = "Kapasitas listrik"
    )
    val fasilitas = ElemenProperti(
        placeholder = "Fasilitas, ex : 2 kamar mandi",
        errorMessage = "",
        defValue = ""
    )
    val deskripsi = ElemenProperti(
        placeholder = "Deskripsi",
        errorMessage = "",
        defValue = ""
    )
    val luasBangunan = ElemenProperti<Long>(
        placeholder = "Luas bangunan",
        errorMessage = "Luas bangunan"
    )

    private var photoId = 0
    private val _photoUris = mutableStateListOf<UriWithId>()
    val photoUris = _photoUris
    fun addPhotoUris(newPhotoUris : Collection<Uri>){
        _photoUris.addAll(
            newPhotoUris.map {
                    uri ->
                UriWithId(
                    uri = uri,
                    id = photoId++
                )
            }
        )
    }
    fun deletePhotoUri(deleteId : Int){
        _photoUris.removeIf { it.id == deleteId }
    }
}

class SubmitKiosViewModelFactory(private val app : Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SubmitKiosViewModel::class.java))
            return SubmitKiosViewModel(app) as T
        throw IllegalArgumentException("Ada bug")
    }
}

data class UriWithId(
    val uri : Uri,
    val id : Int
)