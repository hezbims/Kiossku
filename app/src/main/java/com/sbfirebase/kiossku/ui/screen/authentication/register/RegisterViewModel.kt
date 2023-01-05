package com.sbfirebase.kiossku.ui.screen.authentication.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sbfirebase.kiossku.domain.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel(){

    private val _isRegisterSucceed = MutableLiveData(false)

    private val _sedangMendaftarkanAkun = MutableStateFlow(false)
    val sedangMendaftarkanAkun : StateFlow<Boolean>
        get() = _sedangMendaftarkanAkun

    fun register(
        fullName : String,
        telepon : String,
        email : String,
        password : String,
        confirmPassword : String
    ){
    }
}