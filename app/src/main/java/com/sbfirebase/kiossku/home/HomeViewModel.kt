package com.sbfirebase.kiossku.home

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.authentication.Authenticator
import com.sbfirebase.kiossku.data.GetKiosResponse
import com.sbfirebase.kiossku.data.KiosData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(val app : Application) : AndroidViewModel(app) {
    private val _data = mutableStateOf<List<KiosData?>?>(null)
    val data : State<List<KiosData?>?>
        get() = _data

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val authenticator = Authenticator(app)
            authenticator.refreshToken()

            val token = authenticator.getToken()
            println("Berhasil")
            withContext(Dispatchers.Main) {
                getProductApiClient.getAllProduct(
                    token = app.getString(R.string.header_token_format , token)
                ).enqueue(object : Callback<GetKiosResponse>{
                    override fun onResponse(
                        call: Call<GetKiosResponse>,
                        response: Response<GetKiosResponse>
                    ) {
                        if (response.isSuccessful)
                            _data.value = response.body()!!.data!!
                    }

                    override fun onFailure(call: Call<GetKiosResponse>, t: Throwable) {
                        Toast.makeText(
                            app,
                            "Periksa koneksi internet anda!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
        /*viewModelScope.launch(Dispatchers.IO) {
            val allKiossData = mutableListOf<Kioss>()


            val rawData = IOUtils.toString(
                app.assets.open("data2.tsv"),
                StandardCharsets.UTF_8
            )

            //Log.d("qqq" , rawData.count{ it == '}'}.toString())
            var id = 1L

            for (currentLine in rawData.split('}')) {
                val itemList = currentLine.split('\t')

                allKiossData.add(
                    Kioss(
                        id = id++,
                        judul = itemList[4],
                        alamat = itemList[10],
                        harga = itemList[7],
                        gambar = itemList[0],
                        tipeProperti = itemList[5],
                        fixAtauNego = itemList[8],
                        luasBangunan = itemList[12],
                        luasLahan = itemList[11],
                        kapasitasListrik = itemList[13],
                        phone = itemList[3],
                        email = itemList[2],
                        kiosHeader = itemList[1]
                    )
                )
            }
            withContext(Dispatchers.Main) {
                _data.value = allKiossData
            }
        }

         */
    }

    private val _navigateDataArgument = MutableLiveData<KiosData?>(null)
    val navigateDataArgument : LiveData<KiosData?>
        get() = _navigateDataArgument

    val navigateToDetail : (kioss : KiosData) -> Unit = {
        _navigateDataArgument.value = it
    }
    fun doneNavigateToDetail(){ _navigateDataArgument.value = null }



    init{
        /*api.login(
            email = "ilhamap45@gmail.com",
            password = "milhamap123"
        ).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("qqq" , response.body()?.data?.token.toString())
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("qqq" , "Failure")
            }
        })*/

        /*viewModelScope.launch(Dispatchers.IO){
            val loginResponse = api.login(
                email = "ilhamap45@gmail.com",
                password = "milhamap123"
            ).execute()
            val data = api.getAllProducts(
                token = "Bearer ${loginResponse.body()!!.data.token}"
            ).execute()
            Log.d("qqq" , data.body()?.data?.size.toString())
        }*/
    }

}

class HomeViewModelFactory(private val app : Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(app) as T
        throw java.lang.IllegalArgumentException("Ada Bug")
    }
}