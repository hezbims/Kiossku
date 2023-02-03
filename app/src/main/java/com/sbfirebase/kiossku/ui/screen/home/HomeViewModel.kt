package com.sbfirebase.kiossku.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.data.model.getproduct.KiosDataDto
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.use_case.GetAllProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authManager : AuthManager,
    private val getAllProduct: GetAllProductUseCases
) : ViewModel() {
    private val _uiHomeState = MutableStateFlow(UiHomeState())
    val uiHomeState = _uiHomeState.asStateFlow()

    init {
        loadData()
    }

    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiHomeState.update {
                _uiHomeState.value.copy(getProductApiResponse = AuthorizedApiResponse.Loading())
            }
            _uiHomeState.update {
                it.copy(getProductApiResponse = getAllProduct())
            }
        }
    }

    private val _navigateDataArgument = MutableLiveData<KiosDataDto?>(null)
    val navigateDataArgument : LiveData<KiosDataDto?>
        get() = _navigateDataArgument

    val navigateToDetail : (kioss : KiosDataDto) -> Unit = {
        _navigateDataArgument.value = it
    }
    fun doneNavigateToDetail(){ _navigateDataArgument.value = null }

}

data class UiHomeState(
    val singleKios : KiosDataDto? = null,
    val getProductApiResponse : AuthorizedApiResponse<List<KiosDataDto?>> = AuthorizedApiResponse.Loading()
)