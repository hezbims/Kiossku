package com.sbfirebase.kiossku.ui.screen.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.model.KiosData
import com.sbfirebase.kiossku.domain.repo_interface.IGetProductRepository
import com.sbfirebase.kiossku.route.AllRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getProductRepo : IGetProductRepository,
    private val authManager: AuthManager,
    savedStateHandle : SavedStateHandle
): ViewModel() {
    val product : KiosData =  savedStateHandle[AllRoute.Detail.argName]!!
    val images = product.images.split(",")
    init{
        Log.e("qqq" , images.toString())
    }
    var uiState = MutableStateFlow(DetailUiState())
        private set

}

data class DetailUiState(
    val getNomorTelepon : AuthorizedApiResponse<String>? = null
)