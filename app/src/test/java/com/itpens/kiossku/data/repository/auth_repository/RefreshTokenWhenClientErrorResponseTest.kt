@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.repository.auth_repository

import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.model.auth.FailedRefreshTokenError
import com.itpens.kiossku.data.model.refresh.SuccessfulRefreshTokenResponse
import com.itpens.kiossku.data.repository.AuthRepositoryImpl
import com.itpens.kiossku.domain.ITokenManager
import com.itpens.kiossku.domain.SessionManager
import com.itpens.kiossku.domain.model.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import retrofit2.Response

@DisplayName("AuthRepositoryImpl - refreshToken - client error response")
class RefreshTokenWhenClientErrorResponseTest {

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

    @DisplayName("When the refresh token request receives a client error response then it returns an invalid refresh token error")
    @ParameterizedTest(name = "When the response status code is {0}")
    @ValueSource(ints = [400, 401])
    fun returnsInvalidRefreshTokenErrorWhenStatusCodeIs(statusCode : Int) = runTest {
        coEvery { authApiClient.refreshToken() } returns errorResponse(statusCode)

        val result = sut.refreshToken()

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            FailedRefreshTokenError.InvalidRefreshToken,
            (result as ResultWrapper.Failure).error
        )
        coVerify(exactly = 0) { tokenManager.setUserProfile(any()) }
    }
}
