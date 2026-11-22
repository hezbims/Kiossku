@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.repository.auth_repository

import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.model.auth.TokenData
import com.itpens.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.itpens.kiossku.data.model.refresh.TokenResponseData
import com.itpens.kiossku.data.repository.AuthRepositoryImpl
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.domain.model.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.Response

@DisplayName("AuthRepositoryImpl - refreshToken - successful response")
class RefreshTokenWhenResponseIsSuccessfulTest {

    private lateinit var authApiClient : AuthApi
    private lateinit var tokenManager : ITokenManager
    private lateinit var sut : AuthRepositoryImpl

    @BeforeEach
    fun setup() {
        authApiClient = mockk()
        tokenManager = mockk(relaxed = true)
        sut = AuthRepositoryImpl(
            authApiClient = authApiClient,
            registerErrorMapper = mockk(),
            tokenManager = tokenManager,
            sessionManager = SessionManager()
        )
    }

    private fun successfulRefreshTokenResponse(
        accessToken : String = "accessToken123",
        refreshToken : String = "refreshToken123",
    ) = Response.success(
        SuccessfulRefreshTokenResponse(
            data = TokenResponseData(
                token = accessToken,
                refreshToken = refreshToken,
            )
        )
    )

    @DisplayName("When the refresh token request succeeds then it returns the token data and stores it")
    @Test
    fun returnsTokenDataAndStoresItWhenResponseIsSuccessful() = runTest {
        val accessToken = "accessToken123"
        val refreshToken = "refreshToken123"
        val capturedTokenData = slot<TokenData>()

        coEvery {
            authApiClient.refreshToken()
        } returns successfulRefreshTokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Success)
        assertEquals(
            TokenData(
                accessToken = accessToken,
                refreshToken = refreshToken
            ),
            (result as ResultWrapper.Success).data
        )
        coVerify(exactly = 1) {
            tokenManager.setUserProfile(capture(capturedTokenData))
        }
        assertEquals(
            TokenData(
                accessToken = accessToken,
                refreshToken = refreshToken
            ),
            capturedTokenData.captured
        )
    }
}
