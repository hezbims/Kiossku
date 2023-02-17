package com.sbfirebase.kiossku.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.apiresponse.LogoutApiResponse
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
        if (!uiState.value.sedangLogout) {
            _uiState.update {
                _uiState.value.copy(
                    sedangLogout = true
                )
            }

            viewModelScope.launch(Dispatchers.IO) {
                val logoutResult = authManager.logOut()
                _uiState.update {
                    _uiState.value.copy(
                        sedangLogout = false,
                        isLoggedOut = logoutResult is LogoutApiResponse.Success
                    )
                }
            }
        }
    }

    private fun doneLoggingOut(){
        _uiState.update {
            it.copy(isLoggedOut = false)
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
    val sedangLogout : Boolean = false,
    val isLoggedOut : Boolean = false,
    val getUserResponse : ApiResponse<UserData> = ApiResponse.Loading(),
    val displayDialog : Boolean = false
)

sealed class ProfileScreenEvent {
    object Logout : ProfileScreenEvent()
    object GetUser : ProfileScreenEvent()
    object DoneLoggingOut : ProfileScreenEvent()
    class DoneUpdatingProfile(val newData: UserData) : ProfileScreenEvent()
}