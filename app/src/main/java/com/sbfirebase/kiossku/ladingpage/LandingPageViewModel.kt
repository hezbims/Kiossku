package com.sbfirebase.kiossku.ladingpage

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.sbfirebase.kiossku.authentication.Authenticator
import com.sbfirebase.kiossku.authentication.logout.LogoutResponse
import com.sbfirebase.kiossku.authentication.authApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LandingPageViewModel(private val app : Application) : AndroidViewModel(app) {
    private val _logoutSucceed = MutableLiveData(false)
    val logoutSucceed : LiveData<Boolean>
        get() = _logoutSucceed

    private val authenticator = Authenticator(app)

    val isLoggedIn : Boolean
        get() = authenticator.isLoggedIn()
    fun logout(){
        authApiClient.logout().enqueue(logoutCallback)
    }

    private val logoutCallback = object : Callback<LogoutResponse>{
        override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
            if (response.isSuccessful){
                viewModelScope.launch(Dispatchers.IO) {
                    authenticator.logOut()
                    withContext(Dispatchers.Main) {
                        _logoutSucceed.value = true
                    }
                }
            }
            else
                Toast.makeText(app , "Kesalahan server" , Toast.LENGTH_SHORT).show()
        }

        override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
            Toast.makeText(app , "Gagal logout, periksa internet anda" , Toast.LENGTH_SHORT).show()
        }
    }
}

class LandingPageViewModelFactory(private val app : Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(LandingPageViewModel::class.java))
            return LandingPageViewModel(app) as T
        throw IllegalArgumentException("Ada bug!")
    }
}