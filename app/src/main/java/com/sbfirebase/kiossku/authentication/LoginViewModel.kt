package com.sbfirebase.kiossku.authentication

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.data.LoginResponse
import com.sbfirebase.kiossku.data.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                    saveToken(response.body()!!.data.token)
                    _isLoginSucceed.value = true
                }
            }
            else
                Toast.makeText(app , response.body()!!.message , Toast.LENGTH_LONG).show()
            _isWaitingForResponse.value = false
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(app , "Periksa koneksi anda!" , Toast.LENGTH_LONG).show()
            _isWaitingForResponse.value = false
        }
    }

    private fun saveToken(token : String){
        app.getSharedPreferences(
            app.getString(R.string.shared_preference_name),
            Context.MODE_PRIVATE
        ).edit().apply {
            putString(
                app.getString(R.string.saved_token_key),
                token
            )
            apply()
        }
    }

}