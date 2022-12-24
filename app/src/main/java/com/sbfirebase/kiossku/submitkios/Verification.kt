package com.sbfirebase.kiossku.submitkios

import androidx.compose.runtime.Composable
import com.sbfirebase.kiossku.submitkios.enumdata.ElemenProperti

fun verify(
    datas : List<Pair<String , Boolean>>,
    displayError : (String) -> Unit
) : Boolean{
    // cari elemen yang berisi false
    val result = datas.firstOrNull {
        !it.second
    }

    // kalo ada yang berisi false, maka verifikasi gagal
    result?.let{
        displayError(it.first)
        return false
    }
    return true
}

@Composable
fun <T>toPair(elemenProperti: ElemenProperti<T>) =
    Pair(
        first = elemenProperti.errorMessage,
        second = elemenProperti.getValue()?.toString()?.isNotEmpty() ?: false
    )