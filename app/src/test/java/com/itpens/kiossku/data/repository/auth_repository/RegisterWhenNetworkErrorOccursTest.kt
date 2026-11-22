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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.SocketTimeoutException

@DisplayName("AuthRepositoryImpl - register - network error")
class RegisterWhenNetworkErrorOccursTest {

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

    @DisplayName("When registration fails with a network exception then it returns a general network issue error")
    @Test
    fun returnsNetworkIssueErrorWhenIOExceptionIsThrown() = runTest {
        coEvery { authApiClient.register(any()) } throws IOException()

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(ExternalSystemError.NetworkIssue)
            ),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When registration times out then it returns a general timeout error")
    @Test
    fun returnsTimeoutErrorWhenSocketTimeoutExceptionIsThrown() = runTest {
        coEvery { authApiClient.register(any()) } throws SocketTimeoutException()

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(ExternalSystemError.Timeout)
            ),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When registration fails with an unexpected exception then it returns a general unexpected error")
    @Test
    fun returnsUnexpectedErrorWhenOtherExceptionIsThrown() = runTest {
        coEvery { authApiClient.register(any()) } throws IllegalStateException("boom")

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(ExternalSystemError.Unexpected)
            ),
            (result as ResultWrapper.Failure).error
        )
    }
}
