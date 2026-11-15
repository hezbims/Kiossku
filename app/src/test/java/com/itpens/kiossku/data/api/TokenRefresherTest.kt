@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.api

import com.itpens.kiossku.data.model.auth.TokenData
import com.itpens.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.itpens.kiossku.data.model.refresh.TokenResponseData
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.io.IOException

@DisplayName("TokenRefresher")
class TokenRefresherTest {

    private lateinit var refreshAuthApi : AuthApi
    private lateinit var tokenManager : ITokenManager
    private lateinit var sessionManager : SessionManager
    private lateinit var sut : TokenRefresher

    @BeforeEach
    fun setup(){
        refreshAuthApi = mockk()
        tokenManager = mockk(relaxed = true)
        sessionManager = SessionManager()
        sut = TokenRefresher(
            refreshAuthApi = refreshAuthApi,
            tokenManager = tokenManager,
            sessionManager = sessionManager
        )
    }

    private fun successfulRefreshResponse(
        accessToken : String = "newAccessToken123",
        refreshToken : String = "newRefreshToken123"
    ) = Response.success(
        SuccessfulRefreshTokenResponse(
            data = TokenResponseData(
                token = accessToken,
                refreshToken = refreshToken
            )
        )
    )

    private fun unauthorizedResponse() = Response.error<SuccessfulRefreshTokenResponse>(
        401,
        "unauthorized".toResponseBody("application/json".toMediaType())
    )

    @Test
    @DisplayName("getValidToken: token tersimpan masih valid -> dipakai tanpa refresh")
    fun validStoredTokenUsedDirectly() = runTest {
        coEvery { tokenManager.getValidProfileToken() } returns "stillValidToken"

        val token = sut.getValidToken()

        assertEquals("stillValidToken", token)
        coVerify(exactly = 0) { refreshAuthApi.refreshToken() }
    }

    @Test
    @DisplayName("getValidToken: token tidak ada, refresh sukses -> token baru tersimpan & sesi valid")
    fun refreshSuccessStoresNewToken() = runTest {
        coEvery { tokenManager.getValidProfileToken() } returns null
        coEvery { refreshAuthApi.refreshToken() } returns successfulRefreshResponse()

        val token = sut.getValidToken()

        assertEquals("newAccessToken123", token)
        coVerify(exactly = 1) {
            tokenManager.setUserProfile(
                TokenData(
                    accessToken = "newAccessToken123",
                    refreshToken = "newRefreshToken123"
                )
            )
        }
        assertTrue(sessionManager.isSessionValid.value)
    }

    @Test
    @DisplayName("getValidToken: refresh 401 permanen -> return null & sesi invalid")
    fun refreshUnauthorizedMarksSessionInvalid() = runTest {
        coEvery { tokenManager.getValidProfileToken() } returns null
        coEvery { refreshAuthApi.refreshToken() } returns unauthorizedResponse()

        val token = sut.getValidToken()

        assertNull(token)
        coVerify(exactly = 1) { refreshAuthApi.refreshToken() }
        coVerify(exactly = 0) { tokenManager.setUserProfile(any()) }
        assertFalse(sessionManager.isSessionValid.value)
    }

    @Test
    @DisplayName("getValidToken: semua percobaan retryable (IOException) -> sesi invalid")
    fun exhaustedRetriesMarkSessionInvalid() = runTest {
        coEvery { tokenManager.getValidProfileToken() } returns null
        coEvery { refreshAuthApi.refreshToken() } throws IOException("no internet")

        val token = sut.getValidToken()

        assertNull(token)
        // 3x percobaan (MAX_ATTEMPTS)
        coVerify(exactly = 3) { refreshAuthApi.refreshToken() }
        assertFalse(sessionManager.isSessionValid.value)
    }
}
