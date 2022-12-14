package com.sbfirebase.kiossku.authentication

import android.content.Context
import com.sbfirebase.kiossku.R

class Authenticator(private val context : Context) {
    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.shared_preference_name),
        Context.MODE_PRIVATE
    )!!

    suspend fun saveToken(token : String){
        sharedPreferences.edit().apply {
            putString(
                context.getString(R.string.saved_token_key),
                SavedAuthToken(token).toString()
            )
            commit()
        }
    }

    fun isLoggedIn() =
        sharedPreferences.getString(
            context.getString(R.string.saved_token_key),
            null
        ) != null

    suspend fun logOut(){
        sharedPreferences.edit().apply {
            putString(
                context.getString(R.string.saved_token_key),
                null
            )
            commit()
        }
    }
}