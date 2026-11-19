@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.api.token_refresher

import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.api.TokenRefresher
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

@DisplayName("TokenRefresher - getValidToken")
class TokenRefresherTest {

    private lateinit var refreshAuthApi : AuthApi
    private lateinit var tokenManager : ITokenManager
    private lateinit var sessionManager : SessionManager
    private lateinit var sut : TokenRefresher

    @BeforeEach
    fun setup() {
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
    @DisplayName("When getValidToken is called and the stored token is still valid then it returns the stored token without refreshing")
    fun returnsStoredTokenWhenStoredTokenIsStillValid() = runTest {
        coEvery { tokenManager.getValidProfileToken() } returns "stillValidToken"

        val token = sut.getValidToken()

        assertEquals("stillValidToken", token)
        coVerify(exactly = 0) { refreshAuthApi.refreshToken() }
    }

    @Test
    @DisplayName("When getValidToken is called and the token refresh succeeds then it returns the new access token, stores it, and marks the session valid")
    fun returnsNewAccessTokenWhenRefreshSucceeds() = runTest {
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
    @DisplayName("When getValidToken is called and the refresh token is rejected then it returns null, does not store a token, and marks the session invalid")
    fun returnsNullWhenRefreshTokenIsRejected() = runTest {
        coEvery { tokenManager.getValidProfileToken() } returns null
        coEvery { refreshAuthApi.refreshToken() } returns unauthorizedResponse()

        val token = sut.getValidToken()

        assertNull(token)
        coVerify(exactly = 1) { refreshAuthApi.refreshToken() }
        coVerify(exactly = 0) { tokenManager.setUserProfile(any()) }
        assertFalse(sessionManager.isSessionValid.value)
    }

    @Test
    @DisplayName("When getValidToken is called and all refresh attempts fail with retryable errors then it returns null after three attempts and marks the session invalid")
    fun returnsNullWhenAllRefreshAttemptsFail() = runTest {
        coEvery { tokenManager.getValidProfileToken() } returns null
        coEvery { refreshAuthApi.refreshToken() } throws IOException("no internet")

        val token = sut.getValidToken()

        assertNull(token)
        coVerify(exactly = 3) { refreshAuthApi.refreshToken() }
        assertFalse(sessionManager.isSessionValid.value)
    }
}
