package com.sbfirebase.kiossku.authentication.register

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbfirebase.kiossku.authentication.authApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val app : Application) : AndroidViewModel(app){

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
        _sedangMendaftarkanAkun.value = true
        authApiClient.register(
            RegisterPost(
                fullname = fullName,
                telepon = telepon,
                email = email,
                password = password,
                confirm_password = confirmPassword
            )
        ).enqueue(registerCallback)
    }

    private val registerCallback = object : Callback<List<RegisterResponse>> {
        override fun onResponse(
            call: Call<List<RegisterResponse>>,
            response: Response<List<RegisterResponse>>
        ) {
            if (response.isSuccessful)
                _isRegisterSucceed.value = true
            else
                Toast.makeText(
                    app,
                    response.body()?.first()?.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            _sedangMendaftarkanAkun.value = false
        }

        override fun onFailure(call: Call<List<RegisterResponse>>, t: Throwable) {
            Toast.makeText(
                app ,
                "Gagal tersambung ke server, periksa internet anda!" ,
                Toast.LENGTH_SHORT
            ).show()
            _sedangMendaftarkanAkun.value = false
        }
    }
}

class RegisterViewModelFactory(private val app : Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java))
            return RegisterViewModel(app) as T
        throw IllegalArgumentException("Ada bug")
    }
}