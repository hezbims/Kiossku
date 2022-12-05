package com.sbfirebase.kiossku.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.sbfirebase.kiossku.data.Kioss
import com.sbfirebase.kiossku.data.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

class HomeViewModel(val app : Application) : AndroidViewModel(app) {
    private val _data = MutableLiveData<List<Kioss>>(null)
    val data : LiveData<List<Kioss>>
        get() = _data

    init{
        viewModelScope.launch(Dispatchers.IO) {
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
    }

    private val _navigateDataArgument = MutableLiveData<Kioss?>(null)
    val navigateDataArgument : LiveData<Kioss?>
        get() = _navigateDataArgument

    val navigateToDetail : (kioss : Kioss) -> Unit = {
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

        viewModelScope.launch(Dispatchers.IO){
            val loginResponse = api.login(
                email = "ilhamap45@gmail.com",
                password = "milhamap123"
            ).execute()
            val data = api.getAllProducts(
                token = "Bearer ${loginResponse.body()!!.data.token}"
            ).execute()
            Log.d("qqq" , data.body()?.data?.size.toString())
        }
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