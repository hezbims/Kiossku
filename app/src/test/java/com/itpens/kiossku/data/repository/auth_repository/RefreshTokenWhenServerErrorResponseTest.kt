@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.repository.auth_repository

import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.model.auth.FailedRefreshTokenError
import com.itpens.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.itpens.kiossku.data.repository.AuthRepositoryImpl
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.domain.model.ExternalSystemError
import com.itpens.kiossku.domain.model.ResultWrapper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.Response

@DisplayName("AuthRepositoryImpl - refreshToken - server error response")
class RefreshTokenWhenServerErrorResponseTest {

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

    private fun errorResponse(statusCode : Int) = Response.error<SuccessfulRefreshTokenResponse>(
        statusCode,
        """{"message" : "error"}""".toResponseBody("application/json".toMediaType())
    )

    @DisplayName("When the refresh token request receives an internal server error then it returns an unexpected external system error")
    @Test
    fun returnsUnexpectedErrorWhenHttpStatusIsInternalServerError() = runTest {
        coEvery { authApiClient.refreshToken() } returns errorResponse(500)

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.Other(ExternalSystemError.Unexpected),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When the refresh token request receives a service unavailable response then it returns a service unavailable error")
    @Test
    fun returnsServiceUnavailableErrorWhenHttpStatusIsServiceUnavailable() = runTest {
        coEvery { authApiClient.refreshToken() } returns errorResponse(503)

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.Other(ExternalSystemError.ServiceUnavailable),
            (result as ResultWrapper.Failure).error
        )
    }
}
