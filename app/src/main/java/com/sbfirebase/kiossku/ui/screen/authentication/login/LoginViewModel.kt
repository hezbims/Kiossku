package com.sbfirebase.kiossku.ui.screen.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.domain.AuthManager
import com.sbfirebase.kiossku.domain.apiresponse.LoginApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel(){

    private val _isWaitingForResponse = MutableLiveData(false)
    val isWaitingForResponse : LiveData<Boolean>
        get() = _isWaitingForResponse

    private val _isLoginSucceed = MutableLiveData(false)
    val isLoginSucceed : LiveData<Boolean>
        get() = _isLoginSucceed

    fun authenticate(email : String , password : String){
        _isWaitingForResponse.value = true
        viewModelScope.launch(Dispatchers.IO){
            val loginResult = authManager.login(
                email = email,
                password = password
            )
            withContext(Dispatchers.Main){
                if (loginResult is LoginApiResponse.Success)
                    _isLoginSucceed.value = true
                _isWaitingForResponse.value = false
            }
        }
    }
}