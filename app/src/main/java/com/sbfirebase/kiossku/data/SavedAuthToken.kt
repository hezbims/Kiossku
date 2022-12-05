package com.sbfirebase.kiossku.data

import com.google.gson.Gson
import java.util.Calendar

data class SavedAuthToken(
    val token : String
){
    val savedDate : Calendar = Calendar.getInstance()
    override fun toString() : String =
        Gson().toJson(this)
}
