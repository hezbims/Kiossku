package com.itpens.kiossku.data.api

import com.itpens.kiossku.data.model.auth.TokenData
import com.itpens.kiossku.di.RefreshTokenClient
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bertanggung jawab mendapatkan access token yang masih berlaku dan
 * melakukan refresh token ke server dengan resilience:
 *
 * - Single-flight: banyak request yang gagal bersamaan hanya memicu
 *   satu panggilan refresh (dijaga [Mutex]); sisanya memakai hasilnya.
 * - Retry: maksimal [MAX_ATTEMPTS] percobaan dengan backoff eksponensial
 *   (500ms -> 1s -> 2s), hanya untuk kegagalan sementara (IOException / 5xx).
 * - 401/400 saat refresh berarti refresh token tidak valid lagi, tidak di-retry.
 * - Refresh gagal permanen -> [SessionManager.markSessionInvalid] sehingga UI
 *   bisa redirect ke halaman login.
 *
 * Memakai AuthApi yang dibangun tanpa interceptor ([RefreshTokenClient])
 * supaya refresh tidak ikut lewat AuthHeaderInterceptor (menghindari
 * recursive refresh) maupun TokenAuthenticator.
 */
@Singleton
class TokenRefresher @Inject constructor(
    @param:RefreshTokenClient private val refreshAuthApi : AuthApi,
    private val tokenManager : ITokenManager,
    private val sessionManager : SessionManager
) {
    private val refreshMutex = Mutex()

    private companion object {
        const val MAX_ATTEMPTS = 3
        const val INITIAL_BACKOFF_MILLIS = 500L
    }

    /**
     * Mengembalikan access token yang masih berlaku.
     * Bila token tersimpan tidak ada / kedaluwarsa, refresh dipicu.
     */
    suspend fun getValidToken() : String? =
        tokenManager.getValidProfileToken() ?: refreshTokenWithRetry()

    /**
     * Memaksa refresh meskipun token tersimpan masih terlihat berlaku.
     * Dipakai TokenAuthenticator ketika server menolak token dengan 401.
     */
    suspend fun forceRefresh() : String? =
        refreshMutex.withLock {
            performRefreshWithRetry()
        }

    private suspend fun refreshTokenWithRetry() : String? =
        refreshMutex.withLock {
            // Selama menunggu lock, request lain mungkin sudah berhasil refresh.
            tokenManager.getValidProfileToken()?.let { return it }

            performRefreshWithRetry()
        }

    private suspend fun performRefreshWithRetry() : String? {
        var backoffMillis = INITIAL_BACKOFF_MILLIS

        repeat(MAX_ATTEMPTS){ attempt ->
            when(val outcome = attemptRefresh()){
                is RefreshOutcome.Success -> {
                    sessionManager.markSessionValid()
                    return outcome.token
                }
                RefreshOutcome.Retryable ->
                    if (attempt < MAX_ATTEMPTS - 1)
                        delay(backoffMillis.also { backoffMillis *= 2 })
                RefreshOutcome.Fatal -> {
                    sessionManager.markSessionInvalid()
                    return null
                }
            }
        }

        // Retry habis tanpa kegagalan fatal (semua retryable), sesi tetap
        // tidak bisa dipulihkan
        sessionManager.markSessionInvalid()
        return null
    }

    private suspend fun attemptRefresh() : RefreshOutcome =
        try {
            val response = refreshAuthApi.refreshToken()

            when {
                response.isSuccessful -> {
                    val tokenResponse = response.body()?.data

                    if (tokenResponse == null)
                        RefreshOutcome.Retryable
                    else{
                        tokenManager.setUserProfile(
                            TokenData(
                                accessToken = tokenResponse.token,
                                refreshToken = tokenResponse.refreshToken
                            )
                        )
                        RefreshOutcome.Success(tokenResponse.token)
                    }
                }
                response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ||
                response.code() == HttpURLConnection.HTTP_BAD_REQUEST ||
                response.code() < 500 ->
                    // Refresh token invalid / client error, tidak ada gunanya retry
                    RefreshOutcome.Fatal
                else ->
                    // 5xx, kemungkinan masalah sementara di server
                    RefreshOutcome.Retryable
            }
        } catch (_ : IOException) {
            // Masalah jaringan, layak di-retry
            RefreshOutcome.Retryable
        } catch (_ : Exception){
            RefreshOutcome.Fatal
        }

    private sealed class RefreshOutcome{
        data class Success(val token : String) : RefreshOutcome()
        object Retryable : RefreshOutcome()
        object Fatal : RefreshOutcome()
    }
}
