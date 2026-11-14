package com.itpens.kiossku.domain

import android.app.AlarmManager
import android.content.Context
import android.util.Base64
import androidx.core.content.edit
import com.google.gson.Gson
import com.itpens.kiossku.data.model.auth.TokenData
import com.itpens.kiossku.data.model.auth.UserProfilePreference
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @param:ApplicationContext private val context : Context,
) : ITokenManager {
    private companion object {
        const val USER_PROFILE_KEY = "UserProfile"
        const val SAVED_TOKEN_KEY = "SavedTokenKey123"
        const val SAVED_USER_ID_KEY = "SavedUserIdKey123"
        const val SHARED_PREF_NAME = "SharedPreference1"
        const val ONE_MINUTES = 60 * 1000
        const val ONE_HOUR = 60 * 60 * 1000L

        /**
         * Margin sebelum exp JWT dianggap sudah kedaluwarsa,
         * menutup clock skew device & request yang sedang in-flight.
         */
        const val EXPIRY_SAFETY_MARGIN_MILLIS = 90 * 1000L

        /**
         * Fallback masa berlaku token bila exp tidak ditemukan pada JWT.
         */
        const val DEFAULT_TOKEN_VALIDITY_MILLIS = ONE_HOUR
    }
    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREF_NAME, Context.MODE_PRIVATE
    )!!
    private val gson = Gson()

    override fun getProfileToken() : TokenData? =
        sharedPreferences.getString(USER_PROFILE_KEY, null)?.let { json ->
            try{
                gson.fromJson(json, UserProfilePreference::class.java)?.token
            }catch (_ : Exception){
                null
            }
        }

    override fun getValidProfileToken() : String? {
        val tokenData = getProfileToken() ?: return null

        if (isProfileTokenExpired(tokenData))
            return null

        return tokenData.accessToken
    }

    /**
     * Cek kedaluwarsa access token:
     * 1. Utamakan field "exp" pada payload JWT (epoch detik).
     * 2. Bila tidak ditemukan / tidak parseable, fallback ke
     *    DEFAULT_TOKEN_VALIDITY_MILLIS sejak token disimpan.
     */
    private fun isProfileTokenExpired(tokenData : TokenData) : Boolean {
        val expiryMillis = getTokenExpiryMillis(tokenData.accessToken)

        val savedAtMillis = getProfileSavedAtMillis()

        val effectiveExpiryMillis = expiryMillis
            ?: (savedAtMillis + DEFAULT_TOKEN_VALIDITY_MILLIS)

        // savedAtMillis == 0L (data lama tanpa savedAtMillis) dan tanpa exp
        // berarti tidak bisa dipercaya; anggap kedaluwarsa agar refresh dipicu.
        if (expiryMillis == null && savedAtMillis <= 0L)
            return true

        return System.currentTimeMillis() >=
            effectiveExpiryMillis - EXPIRY_SAFETY_MARGIN_MILLIS
    }

    /**
     * Ekstrak "exp" (epoch detik) dari payload JWT.
     * Mengembalikan null bila token bukan JWT atau tidak punya exp.
     */
    private fun getTokenExpiryMillis(token : String) : Long? {
        return try{
            val parts = token.split(".")

            if (parts.size != 3)
                null
            else {
                val payloadJson = String(
                    Base64.decode(
                        addBase64UrlPadding(parts[1]),
                        Base64.URL_SAFE or Base64.NO_WRAP
                    ),
                    Charsets.UTF_8
                )

                val payload = gson.fromJson(
                    payloadJson,
                    Map::class.java
                )

                (payload?.get("exp") as? Number)?.toLong()?.times(1000)
            }
        }catch (_ : Exception){
            null
        }
    }

    private fun addBase64UrlPadding(segment : String) : String {
        val remainder = segment.length % 4
        return if (remainder == 0)
            segment
        else
            segment + "=".repeat(4 - remainder)
    }

    private fun getProfileSavedAtMillis() : Long =
        sharedPreferences.getString(USER_PROFILE_KEY, null)?.let { json ->
            try{
                gson.fromJson(json, UserProfilePreference::class.java)?.savedAtMillis ?: 0L
            }catch (_ : Exception){
                0L
            }
        } ?: 0L

    override fun getUserId() =
        sharedPreferences.getInt(SAVED_USER_ID_KEY , -1)

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
    @Deprecated("", replaceWith = ReplaceWith("getProfileToken()"))
    override fun getToken() = if (isTokenExpired) null else savedAuthToken?.token

    private val isTokenExpired : Boolean
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

    @Deprecated(message = "", replaceWith = ReplaceWith("setUserProfile"))
    override fun setTokenSync(token : String?){
        sharedPreferences.edit().apply {
            putString(
                SAVED_TOKEN_KEY,
                if (token != null) SavedAuthToken(token).toString() else null
            )
            apply()
        }
    }

    override fun setUserId(userId : Int) {
        sharedPreferences.edit().apply {
            putInt(SAVED_USER_ID_KEY, userId)
            apply()
        }
    }

    override fun setUserProfile(tokenData: TokenData){
        sharedPreferences.edit {
            // legacy fallback
            putString(SAVED_TOKEN_KEY, SavedAuthToken(tokenData.accessToken).toString())

            putString(
                USER_PROFILE_KEY,
                gson.toJson(
                    UserProfilePreference(
                        token = tokenData,
                        savedAtMillis = System.currentTimeMillis()
                    )
                )
            )
        }
    }

    data class SavedAuthToken(
        val token : String,
    ){
        val savedDate : Calendar = Calendar.getInstance()
        override fun toString() : String =
            Gson().toJson(this)
    }
}