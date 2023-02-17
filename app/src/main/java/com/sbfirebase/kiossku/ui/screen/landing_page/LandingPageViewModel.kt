package com.sbfirebase.kiossku.ui.screen.landing_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val authManager : AuthManager
) : ViewModel(){
    private val _apiResponse = MutableStateFlow<ApiResponse<String>>(
        ApiResponse.Loading()
    )
    val apiResponse = _apiResponse.asStateFlow()

    init {
        getToken()
    }

    private fun getToken(){
        viewModelScope.launch(Dispatchers.IO) {
            while(true) {
                delay(500)
                _apiResponse.update { ApiResponse.Loading() }
                _apiResponse.update { authManager.getToken() }
            }
        }
    }

    fun doneNavigating(){
        _apiResponse.update { ApiResponse.Loading() }
    }
}