package com.sbfirebase.kiossku.ui.screen.submitkios.screen.langkahkedua

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.use_case.EmptyValidationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LangkahKeduaViewModel @Inject constructor(
    private val validate : EmptyValidationUseCases
): ViewModel(){
    private val _uiState = MutableStateFlow(LangkahKeduaUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event : LangkahKeduaScreenEvent){
        when (event){
            is LangkahKeduaScreenEvent.OnChangeLuasLahan ->
                if (event.newValue.isValidNumber())
                    _uiState.update { it.copy(luasLahan = event.newValue) }
            is LangkahKeduaScreenEvent.OnChangePanjang ->
                if (event.newValue.isValidNumber())
                    _uiState.update { it.copy(panjang = event.newValue) }
            is LangkahKeduaScreenEvent.OnChangeLebar ->
                if (event.newValue.isValidNumber())
                    _uiState.update { it.copy(lebar = event.newValue) }
            is LangkahKeduaScreenEvent.OnChangeLuasBangunan ->
                if (event.newValue.isValidNumber())
                    _uiState.update { it.copy(luasBangunan = event.newValue) }
            is LangkahKeduaScreenEvent.OnChangeJumlahLantai ->
                if (event.newValue.isValidNumber())
                    _uiState.update { it.copy(jumlahLantai = event.newValue) }
            is LangkahKeduaScreenEvent.OnChangeKapasitasListrik ->
                if (event.newValue.isValidNumber())
                    _uiState.update { it.copy(kapasitasListrik = event.newValue) }
            is LangkahKeduaScreenEvent.OnChangeFasilitas ->
                _uiState.update { it.copy(fasilitas = event.newValue) }
            is LangkahKeduaScreenEvent.OnChangeDeskripsi ->
                _uiState.update { it.copy(deskripsi = event.newValue) }
            LangkahKeduaScreenEvent.OnNavigateNext ->
                _uiState.update { it.copy(navigateNext = true) }
            LangkahKeduaScreenEvent.OnDoneNavigating ->
                _uiState.update { it.copy(navigateNext = false) }
            LangkahKeduaScreenEvent.OnValidateData ->
                validateData()
        }
    }

    private fun validateData(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isValidatingData = true) }

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
                    kapasitasListrikError = kapasitasListrikError,
                    isValidatingData = false
                )
            }

            if (listOf(luasLahanError , panjangError , lebarError ,
                    luasBangunanError , jumlahLantaiError , kapasitasListrikError).all{ it == null})
                onEvent(LangkahKeduaScreenEvent.OnNavigateNext)
        }
    }
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

    val deskripsi : String = "",

    val navigateNext : Boolean = false,

    val isValidatingData : Boolean = false
)

sealed class LangkahKeduaScreenEvent {
    class OnChangeLuasLahan(val newValue : String) : LangkahKeduaScreenEvent()
    class OnChangePanjang(val newValue : String) : LangkahKeduaScreenEvent()
    class OnChangeLebar(val newValue : String) : LangkahKeduaScreenEvent()
    class OnChangeLuasBangunan(val newValue : String) : LangkahKeduaScreenEvent()
    class OnChangeJumlahLantai(val newValue : String) : LangkahKeduaScreenEvent()
    class OnChangeKapasitasListrik(val newValue : String) : LangkahKeduaScreenEvent()
    class OnChangeFasilitas(val newValue : String) : LangkahKeduaScreenEvent()
    class OnChangeDeskripsi(val newValue : String) : LangkahKeduaScreenEvent()
    object OnNavigateNext : LangkahKeduaScreenEvent()
    object OnDoneNavigating : LangkahKeduaScreenEvent()
    object OnValidateData : LangkahKeduaScreenEvent()
}

fun String.isValidNumber() =
    isEmpty() || length <= 9 && last().isDigit()