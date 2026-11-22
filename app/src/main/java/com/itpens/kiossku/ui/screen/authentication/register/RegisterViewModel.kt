package com.itpens.kiossku.ui.screen.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itpens.kiossku.R
import com.itpens.kiossku.data.model.register.RegisterNewUserError
import com.itpens.kiossku.data.model.register.RegisterPost
import com.itpens.kiossku.domain.model.ResultWrapper
import com.itpens.kiossku.domain.repo_interface.IAuthRepository
import com.itpens.kiossku.ui.utils.IToastDisplayer
import com.itpens.kiossku.ui.utils.UiText
import com.itpens.kiossku.ui.utils.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val toastDisplayer: IToastDisplayer
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event : RegisterScreenEvent){
        when(event){
            is RegisterScreenEvent.OnChangeFullName ->
                _uiState.update { it.copy(fullName = event.newValue, fullNameError = null) }
            is RegisterScreenEvent.OnChangeTelepon ->
                _uiState.update { it.copy(telepon = event.newValue, teleponError = null) }
            is RegisterScreenEvent.OnChangeEmail ->
                _uiState.update { it.copy(email = event.newValue, emailError = null) }
            is RegisterScreenEvent.OnChangePassword ->
                _uiState.update { it.copy(password = event.newValue, passwordError = null) }
            RegisterScreenEvent.OnChangePasswordVisibility -> {
                val newValue = !_uiState.value.showPassword
                _uiState.update {
                    it.copy(showPassword = newValue)
                }
            }
            is RegisterScreenEvent.OnChangeConfirmPassword ->
                _uiState.update {
                    it.copy(
                        confirmPassword = event.newValue,
                        confirmPasswordError = null
                    )
                }
            RegisterScreenEvent.OnChangeConfirmPasswordVisibility -> {
                val newValue = !_uiState.value.showConfirmPassword
                _uiState.update {
                    it.copy(showConfirmPassword = newValue)
                }
            }
            RegisterScreenEvent.OnSubmitData ->
                register()
            RegisterScreenEvent.OnRegistrationSuccessHandled ->
                _uiState.update { it.copy(isRegistrationSuccessful = false) }
        }
    }

    private var registerJob : Job? = null

    private fun register() {
        if (registerJob?.isActive == true) return

        _uiState.update {
            it.copy(
                fullNameError = null,
                teleponError = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null,
                generalError = null
            )
        }

        if (_uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.update {
                it.copy(
                    confirmPasswordError = UiText.Resource(
                        R.string.register_password_mismatch
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(isSubmitting = true) }

        registerJob = viewModelScope.launch {
            val result = authRepository.register(
                RegisterPost(
                    fullname = _uiState.value.fullName,
                    telepon = "62${_uiState.value.telepon}",
                    email = _uiState.value.email,
                    password = _uiState.value.password,
                    confirm_password = _uiState.value.confirmPassword
                )
            )

            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            isRegistrationSuccessful = true
                        )
                    }
                    toastDisplayer(UiText.Resource(R.string.register_success))
                }
                is ResultWrapper.Failure -> {
                    _uiState.update { it.withErrors(result.error) }

                    val otherTexts = result.error
                        .filterIsInstance<RegisterNewUserError.Other>()
                        .map { it.toUiText() }

                    if (otherTexts.isNotEmpty())
                        toastDisplayer(
                            if (otherTexts.size == 1)
                                otherTexts.first()
                            else
                                UiText.Joined(otherTexts)
                        )
                }
            }
        }
    }
}

private fun RegisterNewUserError.Other.toUiText(): UiText =
    if (message.isBlank())
        error.toUserMessage()
    else
        UiText.Dynamic(message)

private fun RegisterUiState.withErrors(
    errors: List<RegisterNewUserError>
): RegisterUiState {
    var state = copy(
        isSubmitting = false,
        fullNameError = null,
        teleponError = null,
        emailError = null,
        passwordError = null,
        confirmPasswordError = null,
        generalError = null
    )

    errors.forEach { error ->
        state = when (error) {
            is RegisterNewUserError.Email.Invalid,
            is RegisterNewUserError.Email.AlreadyRegistered ->
                state.copy(emailError = error.message)

            is RegisterNewUserError.PhoneNumber.Invalid,
            is RegisterNewUserError.PhoneNumber.AlreadyRegistered ->
                state.copy(teleponError = error.message)

            is RegisterNewUserError.Password.Invalid ->
                state.copy(passwordError = error.message)

            is RegisterNewUserError.FullName.Empty,
            is RegisterNewUserError.FullName.AlreadyRegistered ->
                state.copy(fullNameError = error.message)

            is RegisterNewUserError.Other -> {
                val newError = error.toUiText()

                state.copy(
                    generalError = state.generalError
                        ?.let { UiText.Joined(listOf(it, newError)) }
                        ?: newError
                )
            }
        }
    }

    return state
}

data class RegisterUiState(
    val fullName : String = "",
    val fullNameError : String? = null,
    val telepon : String = "",
    val teleponError : String? = null,
    val email : String = "",
    val emailError : String? = null,
    val password : String = "",
    val passwordError : String? = null,
    val showPassword : Boolean = false,
    val confirmPassword: String = "",
    val confirmPasswordError : UiText? = null,
    val showConfirmPassword : Boolean = false,
    val isSubmitting : Boolean = false,
    val isRegistrationSuccessful : Boolean = false,
    val generalError : UiText? = null
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
    object OnRegistrationSuccessHandled : RegisterScreenEvent()
}
