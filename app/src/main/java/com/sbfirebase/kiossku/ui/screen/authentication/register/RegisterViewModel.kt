package com.sbfirebase.kiossku.ui.screen.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.constant.ApiMessage
import com.sbfirebase.kiossku.data.model.register.RegisterPost
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.ApiResponse
import com.sbfirebase.kiossku.domain.use_case.validation.ValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val validationuseCase : ValidationUseCase
) : ViewModel(){
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event : RegisterScreenEvent){
        when(event){
            is RegisterScreenEvent.OnChangeFullName ->
                _uiState.update { it.copy(fullName = event.newValue) }
            is RegisterScreenEvent.OnChangeTelepon ->
                _uiState.update { it.copy(telepon = event.newValue) }
            is RegisterScreenEvent.OnChangeEmail ->
                _uiState.update { it.copy(email = event.newValue) }
            is RegisterScreenEvent.OnChangePassword ->
                _uiState.update { it.copy(password = event.newValue) }
            RegisterScreenEvent.OnChangePasswordVisibility -> {
                val newValue = !_uiState.value.showPassword
                _uiState.update {
                    it.copy(showPassword = newValue)
                }
            }
            is RegisterScreenEvent.OnChangeConfirmPassword ->
                _uiState.update { it.copy(confirmPassword = event.newValue) }
            RegisterScreenEvent.OnChangeConfirmPasswordVisibility -> {
                val newValue = !_uiState.value.showConfirmPassword
                _uiState.update {
                    it.copy(showConfirmPassword = newValue)
                }
            }
            RegisterScreenEvent.OnSubmitData ->
                register()
        }
    }

    private var registerJob : Job? = null

    private fun register(){
        if (registerJob == null || registerJob?.isCompleted == true)
            registerJob = viewModelScope.launch(Dispatchers.IO){
                var teleponError = validationuseCase
                    .validateTelepon(_uiState.value.telepon)

                var emailError = validationuseCase
                    .validateEmail(_uiState.value.email)

                val passwordError = validationuseCase
                    .validatePassword(
                        password = _uiState.value.password,
                        confirmPassword = _uiState.value.confirmPassword
                    )

                if (listOf(passwordError , emailError , teleponError).all { it == null })
                    authManager.register(
                        RegisterPost(
                            fullname = _uiState.value.fullName,
                            telepon = "62${_uiState.value.telepon}",
                            email = _uiState.value.email,
                            password = _uiState.value.password,
                            confirm_password = _uiState.value.confirmPassword
                        )
                    ).collect{ response ->
                        teleponError =
                            if (ApiMessage.TELEPHONE_NUMBER_ALREADY_USED == response.errorMessage)
                                "Nomor telepon sudah pernah digunakan"
                            else null
                        emailError =
                            if (ApiMessage.EMAIL_AND_PASSWORD_NOT_MATCH == response.errorMessage)
                                "Email sudah pernah digunakan"
                            else null

                        _uiState.update {
                            it.copy(
                                teleponError = teleponError,
                                emailError = emailError,
                                apiResponse = response
                            )
                        }
                    }
                else _uiState.update {
                    it.copy(
                        teleponError = teleponError,
                        emailError = emailError,
                        passwordError = passwordError
                    )
                }
            }
    }

    fun resetApiState() =
        _uiState.update { it.copy(apiResponse = null) }
}

data class RegisterUiState(
    val fullName : String = "",
    val telepon : String = "",
    val teleponError : String? = null,
    val email : String = "",
    val emailError : String? = null,
    val password : String = "",
    val passwordError : String? = null,
    val showPassword : Boolean = false,
    val confirmPassword: String = "",
    val showConfirmPassword : Boolean = false,
    val apiResponse : ApiResponse<Nothing>? = null
)

sealed class RegisterScreenEvent{
    class OnChangeFullName(val newValue : String) : RegisterScreenEvent()
    class OnChangeTelepon(val newValue : String) : RegisterScreenEvent()
    class OnChangeEmail(val newValue : String) : RegisterScreenEvent()
    class OnChangePassword(val newValue : String) : RegisterScreenEvent()
    object OnChangePasswordVisibility : RegisterScreenEvent()
    class OnChangeConfirmPassword(val newValue : String) : RegisterScreenEvent()
    object OnChangeConfirmPasswordVisibility : RegisterScreenEvent()
    object OnSubmitData : RegisterScreenEvent()
}