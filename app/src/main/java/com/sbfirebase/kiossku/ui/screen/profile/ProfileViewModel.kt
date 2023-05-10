package com.sbfirebase.kiossku.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.model.UserData
import com.sbfirebase.kiossku.domain.use_case.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val getUser : GetUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event : ProfileScreenEvent){
        when (event){
            ProfileScreenEvent.Logout ->
                logout()
            ProfileScreenEvent.DoneLoggingOut ->
                doneLoggingOut()
            ProfileScreenEvent.GetUser ->
                refreshProfile()
            is ProfileScreenEvent.DoneUpdatingProfile -> {
                _uiState.update {
                    it.copy(
                        getUserResponse = ApiResponse.Success(data = event.newData),
                        displayDialog = false
                    )
                }
            }
        }
    }

    private fun logout(){
        if (uiState.value.logoutResponse !is ApiResponse.Loading) {
            _uiState.update {
                _uiState.value.copy(logoutResponse = ApiResponse.Loading())
            }

            viewModelScope.launch(Dispatchers.IO) {
                _uiState.update {
                    _uiState.value.copy(
                        logoutResponse = authManager.logOut()
                    )
                }
            }
        }
    }

    private fun doneLoggingOut(){
        _uiState.update {
            it.copy(logoutResponse = null)
        }
    }

    init{
        refreshProfile()
    }

    private fun refreshProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(getUserResponse = getUser()) }
        }
    }
}

data class ProfileUIState(
    val logoutResponse : ApiResponse<Nothing>? = null,
    val getUserResponse : ApiResponse<UserData> = ApiResponse.Loading(),
    val displayDialog : Boolean = false
)

sealed class ProfileScreenEvent {
    object Logout : ProfileScreenEvent()
    object GetUser : ProfileScreenEvent()
    object DoneLoggingOut : ProfileScreenEvent()
    class DoneUpdatingProfile(val newData: UserData) : ProfileScreenEvent()
}