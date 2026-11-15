@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.repository

import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.model.auth.FailedRefreshTokenError
import com.itpens.kiossku.data.model.auth.TokenData
import com.itpens.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.itpens.kiossku.data.model.refresh.TokenResponseData
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.domain.model.ExternalSystemError
import com.itpens.kiossku.domain.model.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

@DisplayName("AuthRepositoryImpl - refreshToken")
class AuthRepositoryImplRefreshTokenTest {

    private lateinit var authApiClient : AuthApi
    private lateinit var tokenManager : ITokenManager
    private lateinit var sut : AuthRepositoryImpl

    @BeforeEach
    fun setup(){
        authApiClient = mockk()
        tokenManager = mockk(relaxed = true)
        sut = AuthRepositoryImpl(
            authApiClient = authApiClient,
            errorBodyToMessage = mockk(),
            tokenManager = tokenManager,
            sessionManager = SessionManager()
        )
    }

    private fun successfulRefreshTokenResponse(
        accessToken: String = "accessToken123",
        refreshToken: String = "refreshToken123",
    ) = Response.success(
        SuccessfulRefreshTokenResponse(
            data = TokenResponseData(
                token = accessToken,
                refreshToken = refreshToken,
            )
        )
    )

    private fun errorResponse(statusCode : Int) = Response.error<SuccessfulRefreshTokenResponse>(
        statusCode,
        """{"message" : "error"}""".toResponseBody("application/json".toMediaType())
    )

    @DisplayName("Should return invalid refresh token error when status code is 400 or 401")
    @ParameterizedTest(name = "Should return invalid refresh token error when status code is {0}")
    @ValueSource(ints = [400, 401])
    fun shouldReturnInvalidRefreshTokenErrorWhenStatusCodeIs(statusCode : Int) = runTest {
        coEvery { authApiClient.refreshToken() } returns errorResponse(statusCode)

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.InvalidRefreshToken,
            (result as ResultWrapper.Failure).error
        )

        coVerify(exactly = 0) { tokenManager.setUserProfile(any()) }
    }

    @DisplayName("Should return other error (unexpected) when http status is 500 internal server error")
    @Test
    fun shouldReturnOtherErrorWhenHttpStatusIsInternalServerError() = runTest {
        coEvery { authApiClient.refreshToken() } returns errorResponse(500)

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.Other(ExternalSystemError.Unexpected),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("Should return other error (service unavailable) when http status is 503")
    @Test
    fun shouldReturnOtherErrorWhenHttpStatusIsServiceUnavailable() = runTest {
        coEvery { authApiClient.refreshToken() } returns errorResponse(503)

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.Other(ExternalSystemError.ServiceUnavailable),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("Should return other error (network issue) when IOException is thrown")
    @Test
    fun shouldReturnNetworkIssueErrorWhenIOExceptionIsThrown() = runTest {
        coEvery { authApiClient.refreshToken() } throws IOException()

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.Other(ExternalSystemError.NetworkIssue),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("Should return other error (timeout) when SocketTimeoutException is thrown")
    @Test
    fun shouldReturnTimeoutErrorWhenSocketTimeoutExceptionIsThrown() = runTest {
        coEvery { authApiClient.refreshToken() } throws SocketTimeoutException()

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.Other(ExternalSystemError.Timeout),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("Should call setUserProfile with correct parameter when response is successful")
    @Test
    fun shouldCallSetUserProfileWithCorrectParameterWhenResponseIsSuccessful() = runTest {
        val accessToken = "accessToken123"
        val refreshToken = "refreshToken123"
        val capturedTokenData = slot<TokenData>()

        coEvery {
            authApiClient.refreshToken()
        } returns successfulRefreshTokenResponse()

        sut.refreshToken()

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

    @DisplayName("Should return correct token data when api response is successful")
    @Test
    fun shouldReturnCorrectTokenDataWhenApiResponseIsSuccessful() = runTest {
        val accessToken = "accessToken123"
        val refreshToken = "refreshToken123"

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
    }
}
