package com.sbfirebase.kiossku.ui.screen.authentication.confirm_email

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.repo_interface.IAuthRepository
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
class ConfirmEmailViewModel @Inject constructor(
    private val authRepository : IAuthRepository,
    private val toastDisplayer: ToastDisplayer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val email : String = savedStateHandle["email"]!!

    private val _uiState = MutableStateFlow(ConfirmEmailScreenUiState())
    val uiState = _uiState.asStateFlow()

    private var sendTokenJob : Job? = null
    init{
        sendEmailToken()
    }

    private fun sendEmailToken() {
        if (sendTokenJob?.isCompleted == true || sendTokenJob == null)
            sendTokenJob = viewModelScope.launch(Dispatchers.IO) {
                authRepository.sendEmailTokenConfirmation(email = email).collect { response ->
                    _uiState.update { it.copy(sendTokenToEmailResponse = response) }
                }
            }
    }

    private var submitVerificationCode : Job? = null
    private fun submitVerificationCode(){
        if (submitVerificationCode == null || submitVerificationCode?.isCompleted == true)
            submitVerificationCode = viewModelScope.launch(Dispatchers.IO) {
                authRepository.confirmEmail(token = _uiState.value.verificationCode)
                    .collect{ response ->
                        _uiState.update { it.copy(submitTokenResponse = response) }
                        if (response is AuthorizedApiResponse.Failure &&
                                response.errorCode == null)
                            withContext(Dispatchers.Main){
                                toastDisplayer("Gagal mengirim data, periksa internet anda!")
                            }
                    }
            }
    }

    fun onEvent(event : ConfirmEmailScreenEvent){
        when(event){
            ConfirmEmailScreenEvent.OnSendToken ->
                sendEmailToken()
            ConfirmEmailScreenEvent.OnSubmitToken ->
                submitVerificationCode()
            is ConfirmEmailScreenEvent.OnChangeVerificationCode ->
                _uiState.update { it.copy(verificationCode = event.newValue) }
        }
    }
}

data class ConfirmEmailScreenUiState(
    val verificationCode : String = "",
    val sendTokenToEmailResponse : AuthorizedApiResponse<Nothing> = AuthorizedApiResponse.Loading(),
    val submitTokenResponse : AuthorizedApiResponse<Nothing>? = null
)

sealed class ConfirmEmailScreenEvent{
    object OnSendToken : ConfirmEmailScreenEvent()
    object OnSubmitToken : ConfirmEmailScreenEvent()
    class OnChangeVerificationCode(val newValue : String) : ConfirmEmailScreenEvent()
}