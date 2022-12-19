package com.sbfirebase.kiossku.authentication.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.sbfirebase.kiossku.authentication.Authenticator
import com.sbfirebase.kiossku.data.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(val app : Application) : AndroidViewModel(app){

    private val _isWaitingForResponse = MutableLiveData(false)
    val isWaitingForResponse : LiveData<Boolean>
        get() = _isWaitingForResponse

    private val _isLoginSucceed = MutableLiveData(false)
    val isLoginSucceed : LiveData<Boolean>
        get() = _isLoginSucceed

    fun authenticate(email : String , password : String){
        _isWaitingForResponse.value = true
        api.login(email = email , password = password).enqueue(loginCallback)
    }

    private val loginCallback = object : Callback<LoginResponse>{
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful){
                viewModelScope.launch(Dispatchers.IO) {
                    Authenticator(app).saveTokenSync(response.body()!!.data.token)
                    withContext(Dispatchers.Main) {
                        _isLoginSucceed.value = true
                    }
                }
            }
            else
                Toast.makeText(app , "Password dan email tidak cocok" , Toast.LENGTH_LONG).show()
            _isWaitingForResponse.value = false
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(app , "Periksa koneksi anda!" , Toast.LENGTH_LONG).show()
            _isWaitingForResponse.value = false
        }
    }
}

class LoginViewModelFactory(private val app : Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(app) as T
        throw IllegalArgumentException("Ada bug")
    }
}