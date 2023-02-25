package com.sbfirebase.kiossku.ui.screen.submitkios.screen.finalsubmit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.data.model.postproduct.PostKiosData
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.use_case.PostProductUseCase
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua.LangkahKeduaUiState
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahketiga.UriWithId
import com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahpertama.LangkahPertamaUiState
import com.sbfirebase.kiossku.ui.utils.ToastDisplayer
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
class FinalSubmitViewModel @Inject constructor(
    private val postProduct : PostProductUseCase,
    private val displayToast : ToastDisplayer
) : ViewModel(){

    private val _submitState = MutableStateFlow<ApiResponse<Nothing>?>(null)
    val submitState = _submitState.asStateFlow()

    fun onEvent(event : FinalSubmitEvent){
        when (event){
            FinalSubmitEvent.OnDoneNavigating ->
                _submitState.update { null }
            is FinalSubmitEvent.OnStartSubmitData ->
                submitData(
                    postData = createPostKiosData(
                        data1 = event.data1,
                        data2 = event.data2,
                        uriWithIds = event.uriWithIds
                    )
                )
        }
    }

    private var submitJob : Job? = null
    private fun submitData(
        postData : PostKiosData
    ){
        if (submitJob == null || submitJob?.isCompleted == true)
            submitJob = viewModelScope.launch(Dispatchers.IO){
                _submitState.update { ApiResponse.Loading() }

                val postProductResponse = postProduct(data = postData)
                _submitState.update { postProductResponse }

                if (postProductResponse is ApiResponse.Failure)
                    withContext(Dispatchers.Main){
                        displayToast(
                            message = if (postProductResponse.errorCode == null)
                                "Gagal tersambung ke server, periksa internet anda!"
                            else "Token expired"
                        )
                    }
            }
    }

    private fun createPostKiosData(
        data1 : LangkahPertamaUiState,
        data2 : LangkahKeduaUiState,
        uriWithIds : List<UriWithId>
    ) : PostKiosData{
        return PostKiosData(
            judulPromosi = data1.judulPromosi,
            tipeProperti = data1.jenisProperti,
            harga = data1.harga.toInt(),
            waktuPembayaran = data1.waktuPembayaran,
            fixNego = data1.tipePenawaran,
            sewaJual = if (data1.isSewa) "sewa" else "jual",
            lokasi =
                "${data1.provinsi!!.nama};" +
                "${data1.kabupaten!!.nama};" +
                "${data1.kecamatan!!.nama};" +
                data1.kelurahan!!.nama
            ,
            luasLahan = data2.luasLahan.toInt(),
            luasBangunan = data2.luasBangunan.toInt(),
            tingkat = data2.jumlahLantai.toInt(),
            kapasitasListrik = data2.kapasitasListrik.toInt(),
            alamat = "Field alamat tidak ada",
            deskripsi = data2.deskripsi,
            fasilitas = data2.fasilitas,
            panjang = data2.panjang.toInt(),
            lebar = data2.lebar.toInt(),
            images = uriWithIds.map { it.uri }
        )

    }
}

sealed class FinalSubmitEvent {
    object OnDoneNavigating : FinalSubmitEvent()
    class OnStartSubmitData(
        val data1 : LangkahPertamaUiState,
        val data2 : LangkahKeduaUiState,
        val uriWithIds : List<UriWithId>
    ) : FinalSubmitEvent()
}