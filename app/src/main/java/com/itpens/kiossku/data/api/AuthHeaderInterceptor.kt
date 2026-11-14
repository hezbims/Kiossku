package com.itpens.kiossku.data.api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Menambahkan header "Authorization: Bearer <token>" pada setiap request
 * yang lewat OkHttpClient ber-autentikasi.
 *
 * - Token diambil dari TokenManager (USER_PROFILE_KEY); bila tidak ada /
 *   kedaluwarsa, [TokenRefresher] akan refresh dulu (dengan retry).
 * - Request ke endpoint refresh token (path terakhir "token") di-skip
 *   supaya tidak terjadi recursive refresh.
 *
 * Untuk client DaerahApi interceptor ini tidak dipasang.
 */
@Singleton
class AuthHeaderInterceptor @Inject constructor(
    private val tokenRefresher : TokenRefresher
) : Interceptor {

    override fun intercept(chain : Interceptor.Chain) : Response {
        val request = chain.request()

        if (request.url.pathSegments.lastOrNull() == REFRESH_TOKEN_PATH_SEGMENT)
            return chain.proceed(request)

        val token = runBlocking { tokenRefresher.getValidToken() }

        val authenticatedRequest =
            if (token != null)
                request.newBuilder()
                    .header(AUTHORIZATION_HEADER, "Bearer $token")
                    .build()
            else
                request

        return chain.proceed(authenticatedRequest)
    }

    private companion object{
        const val AUTHORIZATION_HEADER = "Authorization"
        const val REFRESH_TOKEN_PATH_SEGMENT = "token"
    }
}
