package com.sbfirebase.kiossku.ui.screen.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.data.model.login.LoginDto
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.ui.utils.ToastDisplayer
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
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val toastDisplayer: ToastDisplayer
) : ViewModel(){
    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event : LoginScreenEvent){
        when(event){
            is LoginScreenEvent.ChangeEmail ->
                _uiState.update { it.copy(email = event.newEmail) }
            is LoginScreenEvent.ChangePassword ->
                _uiState.update { it.copy(password = event.newPassword) }
            is LoginScreenEvent.ChangePasswordVisibility -> {
                val newValue = !_uiState.value.showPassword
                _uiState.update {
                    it.copy(showPassword = newValue)
                }
            }
            is LoginScreenEvent.Authenticate ->
                authenticate()
        }
    }

    private var loginJob : Job? = null

    private fun authenticate() {
        if (loginJob == null || loginJob?.isCompleted == true)
            loginJob = viewModelScope.launch(Dispatchers.IO) {
                authManager.login(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                ).collect{ loginResponse ->
                    _uiState.update {
                        it.copy(loginResponse = loginResponse)
                    }
                    if (loginResponse is ApiResponse.Failure)
                        withContext(Dispatchers.Main){
                            toastDisplayer(loginResponse.errorMessage ?: "Unknown error")
                        }
                }
            }
    }

    fun doneLoggingIn() =
        _uiState.update {
            it.copy(loginResponse = ApiResponse.Failure())
        }
}

data class LoginScreenUiState(
    val email: String = "",
    val password : String = "",
    val showPassword : Boolean = false,
    val loginResponse : ApiResponse<LoginDto>? = null
)

sealed class LoginScreenEvent{
    class ChangeEmail(val newEmail : String) : LoginScreenEvent()
    class ChangePassword(val newPassword : String) : LoginScreenEvent()
    object ChangePasswordVisibility : LoginScreenEvent()
    object Authenticate : LoginScreenEvent()
}