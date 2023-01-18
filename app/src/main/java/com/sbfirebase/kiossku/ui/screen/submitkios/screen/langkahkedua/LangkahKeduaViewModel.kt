package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.use_case.EmptyValidationUseCases
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
class LangkahKeduaViewModel @Inject constructor(
    private val validate : EmptyValidationUseCases
): ViewModel(){
    private val _uiState = MutableStateFlow(LangkahKeduaUiState())
    val uiState = _uiState.asStateFlow()

    fun onChangeLuasLahan(newValue : String){
        if (newValue.isValidNumber())
            _uiState.update {
                it.copy(luasLahan = newValue)
            }
    }

    fun onChangePanjang(newValue : String){
        if (newValue.isValidNumber())
            _uiState.update {
                it.copy(panjang = newValue)
            }
    }

    fun onChangeLebar(newValue: String){
        if (newValue.isValidNumber())
            _uiState.update {
                it.copy(lebar = newValue)
            }
    }

    fun onChangeLuasBangunan(newValue : String){
        if (newValue.isValidNumber())
            _uiState.update {
                it.copy(luasBangunan = newValue)
            }
    }

    fun onChangeJumlahLantai(newValue: String){
        if (newValue.isValidNumber())
            _uiState.update {
                it.copy(jumlahLantai = newValue)
            }
    }

    fun onChangeKapasitasListrik(newValue: String){
        if (newValue.isValidNumber())
            _uiState.update {
                it.copy(kapasitasListrik = newValue)
            }
    }

    fun onChangeFasilitas(newValue: String) =
        _uiState.update {
            it.copy(fasilitas = newValue)
        }

    fun onChangeDeskripsi(newValue: String) =
        _uiState.update {
            it.copy(deskripsi = newValue)
        }

    private val _canNavigate = mutableStateOf(false)
    val canNavigate : State<Boolean>
        get() = _canNavigate

    private var validationJob : Job? = null
    fun validateData(){
        if (validationJob == null || validationJob?.isCompleted == true)
            validationJob = viewModelScope.launch(Dispatchers.IO) {
                val data = uiState.value

                val luasLahanError = validate(data.luasLahan , "Luas lahan")
                val panjangError = validate(data.panjang , "Panjang")
                val lebarError = validate(data.lebar , "Lebar")
                val luasBangunanError = validate(data.luasBangunan , "Luas bangunan")
                val jumlahLantaiError = validate(data.jumlahLantai , "Jumlah lantai")
                val kapasitasListrikError = validate(data.kapasitasListrik , "Kapasitas listrik")

                _uiState.update {
                    it.copy(
                        luasLahanError = luasLahanError,
                        panjangError = panjangError,
                        lebarError = lebarError,
                        luasBangunanError = luasBangunanError,
                        jumlahLantaiError = jumlahLantaiError,
                        kapasitasListrikError = kapasitasListrikError
                    )
                }

                if (listOf(luasLahanError , panjangError , lebarError ,
                    luasBangunanError , jumlahLantaiError , kapasitasListrikError).all{ it == null})
                    withContext(Dispatchers.Main){
                        _canNavigate.value = true
                    }
            }
    }

    fun doneNavigating(){ _canNavigate.value = false }
}
data class LangkahKeduaUiState(
    val luasLahan : String = "",
    val luasLahanError : String? = null,

    val panjang : String = "",
    val panjangError : String? = null,

    val lebar : String = "",
    val lebarError : String? = null,

    val luasBangunan : String = "",
    val luasBangunanError : String? = null,

    val jumlahLantai : String = "",
    val jumlahLantaiError : String? = null,

    val kapasitasListrik : String = "",
    val kapasitasListrikError : String? = null,

    val fasilitas : String = "",

    val deskripsi : String = ""
)

fun String.isValidNumber() =
    isEmpty() || length <= 9 && last().isDigit()