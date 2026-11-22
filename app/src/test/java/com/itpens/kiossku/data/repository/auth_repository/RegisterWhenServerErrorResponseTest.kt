@file:OptIn(ExperimentalCoroutinesApi::class)

package com.itpens.kiossku.data.repository.auth_repository

import com.itpens.kiossku.data.api.AuthApi
import com.itpens.kiossku.data.mapper.RegisterErrorResponseToErrorsMapper
import com.itpens.kiossku.data.model.register.RegisterNewUserError
import com.itpens.kiossku.data.model.register.RegisterPost
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import retrofit2.Response

@DisplayName("AuthRepositoryImpl - register - server error response")
class RegisterWhenServerErrorResponseTest {

    private lateinit var authApiClient : AuthApi
    private lateinit var sut : AuthRepositoryImpl

    @BeforeEach
    fun setup() {
        authApiClient = mockk()
        sut = AuthRepositoryImpl(
            authApiClient = authApiClient,
            registerErrorMapper = RegisterErrorResponseToErrorsMapper(),
            tokenManager = mockk<ITokenManager>(relaxed = true),
            sessionManager = SessionManager()
        )
    }

    private fun registerPost() = RegisterPost(
        fullname = "Budi",
        telepon = "6281234567890",
        email = "budi@example.com",
        password = "secret123",
        confirm_password = "secret123"
    )

    private fun errorResponse(statusCode : Int, body : String) = Response.error<Unit>(
        statusCode,
        body.toResponseBody("application/json".toMediaType())
    )

    @DisplayName("When registration receives an internal server error then it returns a general unexpected error")
    @Test
    fun returnsUnexpectedErrorWhenHttpStatusIsInternalServerError() = runTest {
        coEvery { authApiClient.register(any()) } returns errorResponse(
            statusCode = 500,
            body = """{"message": "Something went wrong"}"""
        )

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    error = ExternalSystemError.Unexpected,
                    message = "Something went wrong"
                )
            ),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When registration receives a service unavailable response then it returns a general service unavailable error")
    @Test
    fun returnsServiceUnavailableErrorWhenHttpStatusIsServiceUnavailable() = runTest {
        coEvery { authApiClient.register(any()) } returns errorResponse(
            statusCode = 503,
            body = """{"message": "Service is down"}"""
        )

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    error = ExternalSystemError.ServiceUnavailable,
                    message = "Service is down"
                )
            ),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When registration is rate limited then it returns a general service unavailable error")
    @ParameterizedTest(name = "When the response status code is {0}")
    @ValueSource(ints = [429])
    fun returnsServiceUnavailableErrorWhenRateLimited(statusCode : Int) = runTest {
        coEvery { authApiClient.register(any()) } returns errorResponse(
            statusCode = statusCode,
            body = """{"message": "Too many requests"}"""
        )

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    error = ExternalSystemError.ServiceUnavailable,
                    message = "Too many requests"
                )
            ),
            (result as ResultWrapper.Failure).error
        )
    }
}
