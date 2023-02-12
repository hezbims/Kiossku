package com.sbfirebase.kiossku.ui.screen.profile.update_profile_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.constant.ApiMessage
import com.sbfirebase.kiossku.data.model.user.UpdateUserDto
import com.sbfirebase.kiossku.domain.apiresponse.AuthorizedApiResponse
import com.sbfirebase.kiossku.domain.model.UserData
import com.sbfirebase.kiossku.domain.use_case.UpdateUserUseCase
import com.sbfirebase.kiossku.domain.use_case.validation.ValidationUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileDialogViewModel @AssistedInject constructor(
    @Assisted initialData : UserData,
    private val updateUser : UpdateUserUseCase,
    private val validationUseCase : ValidationUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileDialogUiState(
        email = initialData.email,
        nomorTelepon = initialData.nomorTelepon,
        namaLengkap = initialData.namaLengkap
    )
    )
    val uiState = _uiState.asStateFlow()

    fun onEvent(event : ProfileDialogEvent){
        when (event){
            is ProfileDialogEvent.ChangeEmail ->
                _uiState.update { it.copy(email = event.newValue) }
            is ProfileDialogEvent.ChangeNamaLengkap ->
                _uiState.update { it.copy(namaLengkap = event.newValue) }
            is ProfileDialogEvent.ChangeNomorTelepon ->
                _uiState.update { it.copy(nomorTelepon = event.newValue) }
            ProfileDialogEvent.Submit -> submitData()
        }
    }

    private fun submitData(){
        viewModelScope.launch (Dispatchers.IO) {
            var emailError = validationUseCase.validateEmail(_uiState.value.email)
            var teleponError = validationUseCase.validateTelepon(_uiState.value.nomorTelepon)

            if (emailError == null && teleponError == null){
                _uiState.update {
                    it.copy(updateUserResponse = AuthorizedApiResponse.Loading())
                }

                val response = updateUser(
                    userNewData = UpdateUserDto(
                        fullname = _uiState.value.namaLengkap,
                        email = _uiState.value.email,
                        telepon = "62${_uiState.value.nomorTelepon}"
                    )
                )

                if (response.errorMessage == ApiMessage.EMAIL_ALREADY_USED)
                    emailError = ApiMessage.EMAIL_ALREADY_USED
                if (response.errorMessage == ApiMessage.TELEPHONE_NUMBER_ALREADY_USED)
                    teleponError = ApiMessage.TELEPHONE_NUMBER_ALREADY_USED
                _uiState.update {
                    it.copy(
                        emailError = emailError,
                        nomorTeleponError = teleponError,
                        updateUserResponse = response
                    )
                }


            }
            else {
                _uiState.update {
                    it.copy(
                        emailError = emailError,
                        nomorTeleponError = teleponError
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory{
        fun create(initialData: UserData) : ProfileDialogViewModel
    }

    companion object {
        fun getFactory(
            assistedFactory : Factory,
            initialData: UserData
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(initialData = initialData) as T
            }
        }
    }
}

data class ProfileDialogUiState(
    val email : String,
    val emailError : String? = null,
    val nomorTelepon : String,
    val nomorTeleponError : String? = null,
    val namaLengkap : String,
    val updateUserResponse : AuthorizedApiResponse<Nothing>? = null
)

sealed class ProfileDialogEvent {
    class ChangeEmail(val newValue : String) : ProfileDialogEvent()
    class ChangeNomorTelepon(val newValue : String) : ProfileDialogEvent()
    class ChangeNamaLengkap(val newValue : String) : ProfileDialogEvent()
    object Submit : ProfileDialogEvent()
}