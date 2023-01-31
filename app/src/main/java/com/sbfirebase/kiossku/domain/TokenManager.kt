package com.sbfirebase.kiossku.domain

import android.app.AlarmManager
import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext private val context : Context,
) {
    private companion object{
        const val SAVED_TOKEN_KEY = "SavedTokenKey123"
        const val SHARED_PREF_NAME = "SharedPreference1"
        const val ONE_MINUTES = 60 * 1000
    }
    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREF_NAME, Context.MODE_PRIVATE
    )!!
    private val savedAuthToken : SavedAuthToken?
        get(){
            return with(
                sharedPreferences.getString(SAVED_TOKEN_KEY, null)
            ){
                if (this != null)
                    Gson().fromJson(this , SavedAuthToken::class.java)
                else
                    null
            }
        }
    fun getToken() = savedAuthToken?.token

    val isTokenExpired : Boolean
        get() {
            val savedTime = savedAuthToken?.savedDate?.timeInMillis
            val currentTime = Calendar.getInstance().timeInMillis

            if (savedTime != null) {
                if (currentTime - savedTime >= AlarmManager.INTERVAL_HOUR - ONE_MINUTES)
                    return true
                return false
            }
            return true
        }

    suspend fun setTokenSync(token : String?){
        sharedPreferences.edit().apply {
            putString(
                SAVED_TOKEN_KEY,
                if (token != null) SavedAuthToken(token).toString() else null
            )
            commit()
        }
    }

    data class SavedAuthToken(
        val token : String?
    ){
        val savedDate : Calendar = Calendar.getInstance()
        override fun toString() : String =
            Gson().toJson(this)
    }
}