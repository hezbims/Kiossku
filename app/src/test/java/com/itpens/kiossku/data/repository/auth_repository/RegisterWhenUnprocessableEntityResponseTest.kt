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
import retrofit2.Response

@DisplayName("AuthRepositoryImpl - register - unprocessable entity response")
class RegisterWhenUnprocessableEntityResponseTest {

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

    private fun errorResponse(body : String) = Response.error<Unit>(
        422,
        body.toResponseBody("application/json".toMediaType())
    )

    @DisplayName("When registration returns multiple known field errors then it maps every error")
    @Test
    fun mapsEveryKnownFieldErrorWhenResponseIsUnprocessable() = runTest {
        val body = """
            {
              "errors": [
                {"type": "EMAIL_INVALID", "message": "Email is invalid"},
                {"type": "EMAIL_ALREADY_USED", "message": "Email already used"},
                {"type": "PHONE_NUMBER_INVALID", "message": "Phone number is invalid"},
                {"type": "PHONE_NUMBER_ALREADY_USED", "message": "Phone number already used"},
                {"type": "PASSWORD_INVALID", "message": "Password is too short"},
                {"type": "FULL_NAME_EMPTY", "message": "Full name is empty"},
                {"type": "FULL_NAME_ALREADY_USED", "message": "Full name already used"}
              ]
            }
        """.trimIndent()

        coEvery { authApiClient.register(any()) } returns errorResponse(body)

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Email.Invalid("Email is invalid"),
                RegisterNewUserError.Email.AlreadyRegistered("Email already used"),
                RegisterNewUserError.PhoneNumber.Invalid("Phone number is invalid"),
                RegisterNewUserError.PhoneNumber.AlreadyRegistered("Phone number already used"),
                RegisterNewUserError.Password.Invalid("Password is too short"),
                RegisterNewUserError.FullName.Empty("Full name is empty"),
                RegisterNewUserError.FullName.AlreadyRegistered("Full name already used")
            ),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When registration returns an unknown error type then it maps it to a general error")
    @Test
    fun mapsUnknownErrorTypeToGeneralError() = runTest {
        val body = """
            {"errors": [{"type": "PASSWORD_CONFIRMATION_MISMATCH", "message": "Passwords do not match"}]}
        """.trimIndent()

        coEvery { authApiClient.register(any()) } returns errorResponse(body)

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    error = ExternalSystemError.Unexpected,
                    message = "Passwords do not match"
                )
            ),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When registration returns a legacy message body then it maps it to a general error")
    @Test
    fun mapsLegacyMessageBodyToGeneralError() = runTest {
        val body = """{"message": "Email already used"}"""

        coEvery { authApiClient.register(any()) } returns errorResponse(body)

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    error = ExternalSystemError.Unexpected,
                    message = "Email already used"
                )
            ),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When registration returns a malformed body then it maps it to a general error without a message")
    @Test
    fun mapsMalformedBodyToGeneralErrorWithoutMessage() = runTest {
        val body = """{"errors": ["""

        coEvery { authApiClient.register(any()) } returns errorResponse(body)

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    error = ExternalSystemError.Unexpected,
                    message = ""
                )
            ),
            (result as ResultWrapper.Failure).error
        )
    }

    @DisplayName("When registration returns an empty error body then it maps it to a general error without a message")
    @Test
    fun mapsEmptyBodyToGeneralErrorWithoutMessage() = runTest {
        coEvery { authApiClient.register(any()) } returns errorResponse("")

        val result = sut.register(registerPost())

        assertTrue(result is ResultWrapper.Failure)
        assertEquals(
            listOf(
                RegisterNewUserError.Other(
                    error = ExternalSystemError.Unexpected,
                    message = ""
                )
            ),
            (result as ResultWrapper.Failure).error
        )
    }
}
