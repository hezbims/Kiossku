package com.sbfirebase.kiossku.ui.screen.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.model.KiosData
import com.sbfirebase.kiossku.domain.use_case.GetUserUseCase
import com.sbfirebase.kiossku.domain.use_case.OpenWhatsappUseCase
import com.sbfirebase.kiossku.route.AllRoute
import com.sbfirebase.kiossku.ui.utils.ToastDisplayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val displayToast : ToastDisplayer,
    private val getUser : GetUserUseCase,
    private val hubungi : OpenWhatsappUseCase,
    savedStateHandle : SavedStateHandle
): ViewModel() {
    val staticData : DetailStaticData
    init {
        val product: KiosData = savedStateHandle[AllRoute.Detail.argName]!!
        val images = product.images.split(",")
        staticData = DetailStaticData(
            product = product,
            images = images
        )
        Log.e("qqqDetailViewModel" , product.images)
    }

    private val _uiState = MutableStateFlow(DetailScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event : DetailScreenEvent){
        when (event){
            DetailScreenEvent.GetNomorTelepon ->
                getNomorTelepon()
            DetailScreenEvent.OpenWhatsapp -> {
                _uiState.update { it.copy(shouldOpenWhatsapp = false) }
                hubungi(_uiState.value.getTeleponResponse?.data!!)
            }
        }
    }

    private fun getNomorTelepon(){
        viewModelScope.launch(Dispatchers.IO) {
            if (_uiState.value.getTeleponResponse is ApiResponse.Success)
                _uiState.update { it.copy(shouldOpenWhatsapp = true) }
            else {
                _uiState.update { it.copy(getTeleponResponse = ApiResponse.Loading()) }
                when (val getUserDataResponse = getUser()) {
                    is ApiResponse.Success ->
                        _uiState.update {
                            it.copy(
                                getTeleponResponse = ApiResponse.Success(
                                    getUserDataResponse.data!!.nomorTelepon
                                ),
                                shouldOpenWhatsapp = true
                            )
                        }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                getTeleponResponse = ApiResponse.Failure()
                            )
                        }
                        withContext(Dispatchers.Main) {
                            displayToast("Gagal menghubungi penjual, periksa internet anda!")
                        }
                    }

                    is ApiResponse.Loading -> throw IllegalArgumentException("Ada bug di detail view model!!")
                }
            }
        }
    }
}

data class DetailScreenUiState(
    val getTeleponResponse : ApiResponse<String>? = null,
    val shouldOpenWhatsapp : Boolean = false
)

data class DetailStaticData(
    val images : List<String>,
    val product : KiosData
)

sealed class DetailScreenEvent {
    object GetNomorTelepon : DetailScreenEvent()
    object OpenWhatsapp : DetailScreenEvent()
}