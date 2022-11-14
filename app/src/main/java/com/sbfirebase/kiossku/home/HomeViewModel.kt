package com.sbfirebase.kiossku.home

import android.app.Application
import androidx.lifecycle.*
import com.sbfirebase.kiossku.data.Kioss
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class HomeViewModel(val app : Application) : AndroidViewModel(app) {
    private val _data = MutableLiveData<List<Kioss>>(null)
    val data : LiveData<List<Kioss>>
        get() = _data

    init{
        viewModelScope.launch(Dispatchers.IO) {
            val allKiossData = mutableListOf<Kioss>()

            val reader = BufferedReader(
                InputStreamReader(app.assets.open("data.tsv"))
            )
            var id = 1L

            while (true) {
                val currentLine = reader.readLine() ?: break
                val itemList = currentLine.split("\t")

                allKiossData.add(
                    Kioss(
                        id = id++,
                        judul = itemList[4],
                        alamat = itemList[10],
                        harga = itemList[7],
                        gambar = itemList.last(),
                        tipeProperti = itemList[5],
                        fixAtauNego = itemList[8],
                        luasBangunan = itemList[12],
                        luasLahan = itemList[11],
                        kapasitasListrik = itemList[13],
                        phone = itemList[3],
                        email = itemList[2]
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
    fun doneNavigateToDetail(){_navigateDataArgument.value = null}
}

class HomeViewModelFactory(private val app : Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(app) as T
        throw java.lang.IllegalArgumentException("Ada Bug")
    }
}