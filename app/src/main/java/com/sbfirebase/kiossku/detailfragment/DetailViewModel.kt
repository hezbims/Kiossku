package com.sbfirebase.kiossku.detailfragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbfirebase.kiossku.data.Kioss

class DetailViewModel(private val app : Application , val currentKioss: Kioss): ViewModel() {
    // TODO: Implement the ViewModel
}

class DetailViewModelFactory(
    private val app : Application ,
    private val currentKioss: Kioss
    ) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(DetailViewModel::class.java))
            return DetailViewModel(app , currentKioss) as T
        throw IllegalArgumentException("Ada bug")
    }
}
