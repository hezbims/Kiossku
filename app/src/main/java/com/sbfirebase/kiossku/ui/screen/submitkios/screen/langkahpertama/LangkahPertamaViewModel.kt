package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.domain.apiresponse.DaerahApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IDaerahRepository
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua.isValidNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LangkahPertamaViewModel @Inject constructor(
    private val daerahRepository : IDaerahRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LangkahPertamaUiState())
    val uiState = _uiState.asStateFlow()

    private val _daerahUiState = MutableStateFlow(DaerahUiState())
    val daerahuiState = _daerahUiState.asStateFlow()
    private var loader : Job? = null

    fun onDataChange(newData : TipeLangkahPertamaData){
        _uiState.update {
            when(newData){
                is TipeLangkahPertamaData.Harga -> {
                    if (newData.newValue.isValidNumber())
                        it.copy(harga = newData.newValue)
                    else it
                }
                is TipeLangkahPertamaData.JudulPromosi -> it.copy(judulPromosi = newData.newValue)
                is TipeLangkahPertamaData.JenisProperti -> it.copy(jenisProperti = newData.newValue)
                is TipeLangkahPertamaData.IsSewa -> it.copy(isSewa = newData.newValue)
                is TipeLangkahPertamaData.TipePenawaran -> it.copy(tipePenawaran = newData.newValue)
                is TipeLangkahPertamaData.WaktuPembayaran -> it.copy(waktuPembayaran = newData.newValue)
                is TipeLangkahPertamaData.Provinsi -> {
                    if (it.provinsi != newData.newValue)
                        onLoadDaerah(TipeLoadedDaerah.Kabupaten(newData.newValue!!.id))
                    it.copy(provinsi = newData.newValue)
                }
                is TipeLangkahPertamaData.Kabupaten -> {
                    if (it.kabupaten != newData.newValue)
                        onLoadDaerah(TipeLoadedDaerah.Kecamatan(newData.newValue!!.id))
                    it.copy(kabupaten = newData.newValue)
                }
                is TipeLangkahPertamaData.Kecamatan -> {
                    if (it.kecamatan != newData.newValue)
                        onLoadDaerah(TipeLoadedDaerah.Kelurahan(newData.newValue!!.id))
                    it.copy(kelurahan = newData.newValue)
                }
                is TipeLangkahPertamaData.Kelurahan -> it.copy(kelurahan = newData.newValue)
            }
        }
    }

    fun onLoadDaerah(data : TipeLoadedDaerah){
        loader?.apply { if(isActive) cancel() }
        loader = viewModelScope.launch(Dispatchers.IO) {
            when (data) {
                is TipeLoadedDaerah.Kabupaten -> {
                    daerahRepository.getKabupaten(data.idProvinsi).collect { response ->
                        _daerahUiState.update {
                            it.copy(
                                kabupaten = response,
                                kecamatan = null,
                                kelurahan = null
                            )
                        }
                    }
                }
                is TipeLoadedDaerah.Kecamatan -> {
                    daerahRepository.getKecamatan(data.idKabupaten).collect { response ->
                        _daerahUiState.update {
                            it.copy(
                                kecamatan = response,
                                kelurahan = null
                            )
                        }
                    }
                }
                is TipeLoadedDaerah.Kelurahan -> {
                    daerahRepository.getKelurahan(data.idKecamatan).collect{ response ->
                        _daerahUiState.update {
                            it.copy(kelurahan = response)
                        }
                    }
                }
                TipeLoadedDaerah.Provinsi -> {
                    daerahRepository.getProvinsi().collect{ response ->
                        _daerahUiState.update {
                            it.copy(
                                provinsi = response,
                                kabupaten = null,
                                kecamatan = null,
                                kelurahan = null
                            )
                        }
                    }
                }
            }
        }
    }

    init{
        Log.e("qqq" , "BERHASIL MEMBUATA VIEW MODEL")
        onLoadDaerah(TipeLoadedDaerah.Provinsi)

    }
}

data class DaerahUiState(
    val provinsi : DaerahApiResponse? = DaerahApiResponse.Loading,
    val kabupaten : DaerahApiResponse? = null,
    val kecamatan : DaerahApiResponse? = null,
    val kelurahan : DaerahApiResponse? = null
)

sealed class TipeLangkahPertamaData{
    class JudulPromosi(val newValue : String) : TipeLangkahPertamaData()
    class JenisProperti(val newValue: String) : TipeLangkahPertamaData()
    class Harga(val newValue : String) : TipeLangkahPertamaData()
    class TipePenawaran(val newValue : String) : TipeLangkahPertamaData()
    class WaktuPembayaran(val newValue : String) : TipeLangkahPertamaData()
    class IsSewa(val newValue : Boolean) : TipeLangkahPertamaData()
    class Provinsi(val newValue : Daerah?) : TipeLangkahPertamaData()
    class Kabupaten(val newValue : Daerah?) : TipeLangkahPertamaData()
    class Kecamatan(val newValue : Daerah?) : TipeLangkahPertamaData()
    class Kelurahan(val newValue : Daerah?) : TipeLangkahPertamaData()
}

sealed class TipeLoadedDaerah{
    object Provinsi : TipeLoadedDaerah()
    class Kabupaten(val idProvinsi : String) : TipeLoadedDaerah()
    class Kecamatan(val idKabupaten : String): TipeLoadedDaerah()
    class Kelurahan(val idKecamatan : String) : TipeLoadedDaerah()
}

data class LangkahPertamaUiState(
    val judulPromosi : String = "",
    val jenisProperti : String = "",
    val harga : String = "",
    val tipePenawaran : String = "",
    val waktuPembayaran: String = "/tahun",
    val isSewa : Boolean = true,
    val provinsi : Daerah? = null,
    val kabupaten : Daerah? = null,
    val kecamatan : Daerah? = null,
    val kelurahan : Daerah? = null
)