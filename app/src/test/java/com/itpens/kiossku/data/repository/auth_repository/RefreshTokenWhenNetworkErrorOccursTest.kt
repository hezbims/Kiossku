@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.repository.auth_repository

import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.model.auth.FailedRefreshTokenError
import com.itpens.kiossku.data.repository.AuthRepositoryImpl
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.domain.model.ExternalSystemError
import com.itpens.kiossku.domain.model.ResultWrapper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.SocketTimeoutException

@DisplayName("AuthRepositoryImpl - refreshToken - network error")
class RefreshTokenWhenNetworkErrorOccursTest {

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

    @DisplayName("When the refresh token request fails with a network exception then it returns a network issue error")
    @Test
    fun returnsNetworkIssueErrorWhenIOExceptionIsThrown() = runTest {
        coEvery { authApiClient.refreshToken() } throws IOException()

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.Other(ExternalSystemError.NetworkIssue),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When the refresh token request times out then it returns a timeout error")
    @Test
    fun returnsTimeoutErrorWhenSocketTimeoutExceptionIsThrown() = runTest {
        coEvery { authApiClient.refreshToken() } throws SocketTimeoutException()

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.Other(ExternalSystemError.Timeout),
            (result as ResultWrapper.Failure).error
        )
    }
}
