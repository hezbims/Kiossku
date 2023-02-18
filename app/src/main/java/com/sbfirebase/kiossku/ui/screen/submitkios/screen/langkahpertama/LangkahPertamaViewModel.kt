package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.data.api.Daerah
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IDaerahRepository
import com.sbfirebase.kiossku.domain.use_case.EmptyValidationUseCases
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua.isValidNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LangkahPertamaViewModel @Inject constructor(
    private val daerahRepository : IDaerahRepository,
    private val validateEmpty : EmptyValidationUseCases
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
                    it.copy(kecamatan = newData.newValue)
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
        onLoadDaerah(TipeLoadedDaerah.Provinsi)
    }

    private val _canNavigate = mutableStateOf(false)
    val canNavigate : State<Boolean>
        get() = _canNavigate

    private var validationJob : Job? = null
    fun validate(){
        if (validationJob == null || validationJob?.isCompleted == true) {
            validationJob = viewModelScope.launch(Dispatchers.IO) {
                uiState.value.let {
                    val judulPromosiError = validateEmpty(data = it.judulPromosi , "Judul promosi")
                    val jenisPropertiError = validateEmpty(data = it.jenisProperti , "Jenis properti")
                    val hargaError = validateEmpty(data = it.harga , "Harga")
                    val tipePenawaranError = validateEmpty(data = it.tipePenawaran , "Tipe penawaran")
                    val provinsiError = validateEmpty(data = it.provinsi?.nama , "Provinsi")
                    val kabupatenError = validateEmpty(data = it.kabupaten?.nama , namaField = "Kabupaten")
                    val kecamatanError = validateEmpty(data = it.kecamatan?.nama , namaField = "Kecamatan")
                    val kelurahanError = validateEmpty(data = it.kelurahan?.nama , namaField = "Kelurahan")

                    _uiState.update { currentState ->
                        currentState.copy(
                            judulPromosiError = judulPromosiError,
                            jenisPropertiError = jenisPropertiError,
                            hargaError = hargaError,
                            tipePenawaranError = tipePenawaranError,
                            provinsiError = provinsiError,
                            kabupatenError = kabupatenError,
                            kecamatanError = kecamatanError,
                            kelurahanError = kelurahanError
                        )
                    }
                    if (listOf(judulPromosiError , jenisPropertiError , hargaError ,
                        tipePenawaranError , provinsiError , kabupatenError , kecamatanError ,
                        kelurahanError).all { errorMessage -> errorMessage == null }){
                            withContext(Dispatchers.Main){
                                _canNavigate.value = true
                            }
                    }
                }
            }
        }
    }

    fun doneNavigating(){ _canNavigate.value = false }
}

data class DaerahUiState(
    val provinsi : ApiResponse<List<Daerah?>>? = ApiResponse.Loading(),
    val kabupaten : ApiResponse<List<Daerah?>>? = null,
    val kecamatan : ApiResponse<List<Daerah?>>? = null,
    val kelurahan : ApiResponse<List<Daerah?>>? = null
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
    val judulPromosiError : String? = null,

    val jenisProperti : String = "",
    val jenisPropertiError : String? = null,

    val harga : String = "",
    val hargaError : String? = null,

    val tipePenawaran : String = "",
    val tipePenawaranError : String? = null,

    val waktuPembayaran: String = "tahunan",
    val isSewa : Boolean = true,
    val provinsi : Daerah? = null,
    val provinsiError : String? = null,
    val kabupaten : Daerah? = null,
    val kabupatenError : String? = null,
    val kecamatan : Daerah? = null,
    val kecamatanError : String? = null,
    val kelurahan : Daerah? = null,
    val kelurahanError : String? = null,
)