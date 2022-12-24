package com.sbfirebase.kiossku.submitkios.enumdata

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow

class ElemenProperti<T>(val placeholder : String , val errorMessage : String , defValue : T? = null){
    private val stateFlow = MutableStateFlow<T?>(defValue)

    @Composable
    fun getValue() = stateFlow.collectAsState().value

    fun setValue(newValue : T?){ stateFlow.value = newValue }
}