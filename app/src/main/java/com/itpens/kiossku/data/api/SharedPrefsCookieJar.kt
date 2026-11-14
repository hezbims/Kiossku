package com.itpens.kiossku.data.api

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

/**
 * CookieJar yang persisten ke SharedPreferences, dipakai bersama oleh semua
 * OkHttpClient Kiossku (kecuali DaerahApi) supaya cookie sesi — yang
 * dibutuhkan endpoint refresh token — tersimpan dan ikut terkirim antar
 * instance client maupun setelah proses mati.
 */
@Singleton
class SharedPrefsCookieJar @Inject constructor(
    @ApplicationContext context : Context
) : CookieJar {

    private val sharedPreferences : SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    /**
     * Cache in-memory per host agar tidak parse JSON di setiap request.
     * URL diajak match oleh OkHttp sendiri lewat Cookie.matches(url),
     * jadi cukup menyimpan cookie per host asal (url pada saat disimpan).
     */
    private val cookieCache = ConcurrentHashMap<String, MutableList<Cookie>>()

    init {
        loadPersistedCookies()
    }

    override fun saveFromResponse(url : HttpUrl, cookies : List<Cookie>) {
        if (cookies.isEmpty())
            return

        val host = url.host

        synchronized(this){
            val existing = cookieCache[host] ?: mutableListOf()

            cookies.forEach{ newCookie ->
                existing.removeAll { it.name == newCookie.name }
                existing.add(newCookie)
            }

            // Cookie expired / max-age habis tidak perlu disimpan
            existing.removeAll { it.expiresAt <= System.currentTimeMillis() }

            cookieCache[host] = existing

            persistCookies(host, existing)
        }
    }

    override fun loadForRequest(url : HttpUrl) : List<Cookie> =
        synchronized(this){
            cookieCache.flatMap{ it.value }
                .filter { it.matches(url) && it.expiresAt > System.currentTimeMillis() }
        }

    private fun loadPersistedCookies() {
        val allEntries = sharedPreferences.all

        allEntries.forEach{ (host, value) ->
            if (value is String){
                try{
                    val cookies : MutableList<Cookie> = gson.fromJson(
                        value,
                        object : TypeToken<MutableList<Cookie>>(){}.type
                    )

                    cookieCache[host] = cookies
                }catch (_ : Exception){
                    // Data korup / format lama, abaikan
                }
            }
        }
    }

    private fun persistCookies(host : String, cookies : List<Cookie>) {
        sharedPreferences.edit {
            putString(host, gson.toJson(cookies))
        }
    }

    private companion object{
        const val SHARED_PREF_NAME = "KiosskuCookieJar"
    }
}
