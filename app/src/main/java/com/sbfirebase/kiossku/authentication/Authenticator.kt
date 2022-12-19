package com.sbfirebase.kiossku.authentication

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.sbfirebase.kiossku.R
import retrofit2.Response
import java.util.Calendar

class Authenticator(private val context : Context) {
    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.shared_preference_name),
        Context.MODE_PRIVATE
    )!!

    fun getToken(): String {
        val savedAuthToken = sharedPreferences.getString(
            context.getString(R.string.saved_token_key),
            null
        )!!

        return Gson().fromJson(savedAuthToken, SavedAuthToken::class.java).token
    }

    private fun isTokenExpired() : Boolean{
        val savedAuthToken = sharedPreferences.getString(
            context.getString(R.string.saved_token_key),
            null
        )!!

        val savedTime = Gson().fromJson(savedAuthToken, SavedAuthToken::class.java)
            .savedDate
            .timeInMillis
        val currentTime = Calendar.getInstance().timeInMillis

        if (currentTime - savedTime >= AlarmManager.INTERVAL_HOUR)
            return true
        return false
    }

    suspend fun saveTokenSync(token : String){
        sharedPreferences.edit().apply {
            putString(
                context.getString(R.string.saved_token_key),
                SavedAuthToken(token).toString()
            )
            commit()
        }
    }

    fun saveTokenAsync(token : String){
        sharedPreferences.edit().apply {
            putString(
                context.getString(R.string.saved_token_key),
                SavedAuthToken(token).toString()
            )
            apply()
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

    suspend fun refreshToken(){
        if (isTokenExpired()) {
            lateinit var refreshTokenResponse: Response<RefreshTokenResponse>
            try {
                refreshTokenResponse = authApiClient.refreshToken().execute()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Periksa internet anda!", Toast.LENGTH_SHORT).show()
            }

            Log.d("qqq", refreshTokenResponse.errorBody()!!.string())
            if (!refreshTokenResponse.isSuccessful)
                throw RuntimeException("Ada bug ketika merefresh token")

            saveTokenSync(refreshTokenResponse.body()!!.data!!.token!!)
            println("Berhasil1")
        }
    }
}