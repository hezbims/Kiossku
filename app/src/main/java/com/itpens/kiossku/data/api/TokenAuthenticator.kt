package com.itpens.kiossku.data.api

import com.itpens.kiossku.domain.ITokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lapisan kedua setelah [AuthHeaderInterceptor]: dipicu otomatis oleh OkHttp
 * ketika server membalas 401 (token dicabut / sudah di-rotate di server
 * padahal secara lokal masih terlihat berlaku).
 *
 * - Bila token tersimpan sudah berubah (request lain sempat refresh),
 *   langsung retry dengan token itu tanpa refresh ulang.
 * - Bila masih sama, paksa refresh lalu retry.
 * - Bila refresh gagal / token hasil refresh sama dengan yang ditolak,
 *   berikan response apa adanya agar tidak terjadi loop retry tak berujung.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRefresher : TokenRefresher,
    private val tokenManager : ITokenManager
) : Authenticator {

    override fun authenticate(route : Route?, response : Response) : Request? {
        if (responseCount(response) >= MAX_AUTHENTICATION_RETRY)
            return null

        val previousAuthHeader = response.request.header(AUTHORIZATION_HEADER)

        // Token mungkin sudah diperbarui oleh request lain yang mendapat 401
        val latestStoredToken = tokenManager.getProfileToken()?.accessToken

        if (latestStoredToken != null && "Bearer $latestStoredToken" != previousAuthHeader)
            return buildRequestWithToken(response.request, latestStoredToken)

        val refreshedToken = runBlocking { tokenRefresher.forceRefresh() }
            ?: return null

        // Refresh menghasilkan token yang sama dengan yang ditolak server,
        // retry hanya akan menghasilkan 401 lagi
        if ("Bearer $refreshedToken" == previousAuthHeader)
            return null

        return buildRequestWithToken(response.request, refreshedToken)
    }

    private fun buildRequestWithToken(request : Request, token : String) =
        request.newBuilder()
            .header(AUTHORIZATION_HEADER, "Bearer $token")
            .build()

    private fun responseCount(response : Response) : Int {
        var count = 1
        var priorResponse = response.priorResponse

        while (priorResponse != null){
            count++
            priorResponse = priorResponse.priorResponse
        }

        return count
    }

    private companion object{
        const val AUTHORIZATION_HEADER = "Authorization"
        const val MAX_AUTHENTICATION_RETRY = 2
    }
}
