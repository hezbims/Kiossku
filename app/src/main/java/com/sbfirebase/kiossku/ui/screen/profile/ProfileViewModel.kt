package com.sbfirebase.kiossku.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.LogoutApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState = _uiState.asStateFlow()

    fun logout(){
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
}

data class ProfileUIState(
    val sedangLogout : Boolean = false,
    val isLoggedOut : Boolean = false
)