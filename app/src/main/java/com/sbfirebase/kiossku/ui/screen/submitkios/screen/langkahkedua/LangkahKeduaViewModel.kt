package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LangkahKeduaViewModel @Inject constructor(): ViewModel(){
    private val _langkahKeduaUiState = MutableStateFlow(LangkahDuaUiState())
    val langkahKeduaUiState = _langkahKeduaUiState.asStateFlow()

    fun onChangeLuasLahan(newValue : String){
        if (newValue.isValidNumber())
            _langkahKeduaUiState.update {
                it.copy(luasLahan = newValue)
            }
    }

    fun onChangePanjang(newValue : String){
        if (newValue.isValidNumber())
            _langkahKeduaUiState.update {
                it.copy(panjang = newValue)
            }
    }

    fun onChangeLebar(newValue: String){
        if (newValue.isValidNumber())
            _langkahKeduaUiState.update {
                it.copy(lebar = newValue)
            }
    }

    fun onChangeLuasBangunan(newValue : String){
        if (newValue.isValidNumber())
            _langkahKeduaUiState.update {
                it.copy(luasBangunan = newValue)
            }
    }

    fun onChangeJumlahLantai(newValue: String){
        if (newValue.isValidNumber())
            _langkahKeduaUiState.update {
                it.copy(jumlahLantai = newValue)
            }
    }

    fun onChangeKapasitasListrik(newValue: String){
        if (newValue.isValidNumber())
            _langkahKeduaUiState.update {
                it.copy(kapasitasListrik = newValue)
            }
    }

    fun onChangeFasilitas(newValue: String) =
        _langkahKeduaUiState.update {
            it.copy(fasilitas = newValue)
        }

    fun onChangeDeskripsi(newValue: String) =
        _langkahKeduaUiState.update {
            it.copy(deskripsi = newValue)
        }
}
data class LangkahDuaUiState(
    val luasLahan : String = "",
    val panjang : String = "",
    val lebar : String = "",
    val luasBangunan : String = "",
    val jumlahLantai : String = "",
    val kapasitasListrik : String = "",
    val fasilitas : String = "",
    val deskripsi : String = ""
)

fun String.isValidNumber() =
    isEmpty() || length <= 9 && last().isDigit()