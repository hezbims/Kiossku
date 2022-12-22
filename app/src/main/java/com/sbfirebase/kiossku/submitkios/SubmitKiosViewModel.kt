package com.sbfirebase.kiossku.submitkios

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbfirebase.kiossku.submitkios.enumdata.*

class SubmitKiosViewModel(private val app : Application) : AndroidViewModel(app) {
    val sistemPembayaran = ElemenProperti<SistemPembayaran>("Pilih sistem pembayaran")
    val judulPromosi = ElemenProperti<String>("Judul promosi")
    val jenisProperti = ElemenProperti<JenisProperti>("Pilih jenis properti")
    val waktuPembayaran = ElemenProperti("Pilih rentang pembayaran" , WaktuPembayaran.TAHUNAN)
    val tipePenawaran = ElemenProperti<TipePenawaran>("Pilih tipe harga")
    val harga = ElemenProperti<Long>("Harga")
    val luasLahan = ElemenProperti<Long>("Luas lahan")
    val panjang = ElemenProperti<Long>("Panjang")
    val lebar = ElemenProperti<Long>("Lebar")
    val jumlahLantai = ElemenProperti<Long>("Jumlah lantai")
    val kapasitasListrik = ElemenProperti<Long>("Kapasitas Listrik")
    val fasilitas = ElemenProperti<String>("Fasilitas, ex : 2 kamar mandi")
    val deskripsi = ElemenProperti<String>("Deskripsi")
    val luasBangunan = ElemenProperti<Long>("Luas bangunan")

    private var photoId = 0
    private val _photoUris = mutableStateListOf<UriWithId>()
    val photoUris = _photoUris
    fun addPhotoUris(newPhotoUris : Collection<Uri>){
        _photoUris.addAll(
            newPhotoUris.mapIndexed {
                    index, uri ->
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